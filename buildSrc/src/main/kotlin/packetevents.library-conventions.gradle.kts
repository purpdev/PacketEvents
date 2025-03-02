import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.util.Node

plugins {
    `java-library`
    `maven-publish`
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
}

val isShadow = project.pluginManager.hasPlugin("com.gradleup.shadow")

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
}

java {
    withSourcesJar()
    withJavadocJar()
    disableAutoTargetJvm()
}

tasks {
    withType<JavaCompile> {
        sequenceOf("unchecked", "deprecation", "removal")
            .forEach { options.compilerArgs.add("-Xlint:$it") }

        options.encoding = Charsets.UTF_8.name()
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 8
    }

    javadoc {
        title = "packetevents-${project.name} v${rootProject.version}"
        options.encoding = Charsets.UTF_8.name()
        options.overview = rootProject.file("buildSrc/src/main/resources/javadoc-overview.html").toString()
        setDestinationDir(file("${project.layout.buildDirectory.asFile.get()}/docs/javadoc"))
        options {
            (this as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching(listOf("plugin.yml", "bungee.yml", "velocity-plugin.json", "fabric.mod.json")) {
            expand("version" to project.version)
        }
    }

    jar {
        archiveClassifier = "default"
    }

    defaultTasks("build")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            groupId = project.group as String
            artifactId = "packetevents-" + project.name
            version = rootProject.ext["versionNoHash"] as String

            if (isShadow) {
                artifact(project.tasks.withType<ShadowJar>().getByName("shadowJar").archiveFile)

                val allDependencies = project.provider {
                    project.configurations.getByName("shadow").allDependencies
                        .filter { it is ProjectDependency || it !is SelfResolvingDependency }
                }

                pom {
                    withXml {
                        val (libraryDeps, projectDeps) = allDependencies.get().partition { it !is ProjectDependency }
                        val dependenciesNode = asNode().get("dependencies") as? Node ?: asNode().appendNode("dependencies")

                        libraryDeps.forEach {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                            dependencyNode.appendNode("scope", "compile")
                        }

                        // project dependencies are other packetevents subprojects
                        // which this subproject depends on, so it's fine to assume some stuff here
                        projectDeps.forEach {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", "packetevents-" + it.name)
                            dependencyNode.appendNode("version", rootProject.ext["versionNoHash"])
                            dependencyNode.appendNode("scope", "compile")
                        }
                    }
                }

                artifact(tasks["sourcesJar"])
            } else {
                from(components["java"])
            }

            pom {
                name = "${rootProject.name}-${project.name}"
                description = rootProject.description
                url = "https://github.com/retrooper/packetevents"

                licenses {
                    license {
                        name = "GPL-3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"
                    }
                }

                developers {
                    developer {
                        id = "retrooper"
                        name = "Retrooper"
                        email = "retrooperdev@gmail.com"
                    }
                }

                scm {
                    connection = "scm:git:https://github.com/retrooper/packetevents.git"
                    developerConnection = "scm:git:https://github.com/retrooper/packetevents.git"
                    url = "https://github.com/retrooper/packetevents/tree/2.0"
                }
            }
        }
    }

    repositories {
        maven {
            val snapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"
            val releaseUrl = "https://repo.codemc.io/repository/maven-releases/"

            // Check which URL should be used
            url = uri(if ((version as String).endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

            val mavenUsername = System.getenv("retrooper_username") ?: return@maven
            val mavenPassword = System.getenv("retrooper_password") ?: return@maven

            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}

// So that SNAPSHOT is always the latest SNAPSHOT
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
}

val taskNames = gradle.startParameter.taskNames
if (taskNames.any { it.contains("build") }
    && taskNames.any { it.contains("publish") }) {
    throw IllegalStateException("Cannot build and publish at the same time.")
}
