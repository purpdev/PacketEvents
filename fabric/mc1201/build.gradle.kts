plugins {
    alias(libs.plugins.fabric.loom)
}

repositories {
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }
}

val minecraft_version: String by project
val parchment_minecraft_version: String by project
val parchment_mappings: String by project
val loader_version: String by project

dependencies {
    implementation(libs.bundles.adventure)
    implementation(project(":api"))
    implementation(project(":netty-common"))
    implementation(project(":fabric"))

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })

    modImplementation("net.fabricmc:fabric-loader:$loader_version")
}

tasks {
    withType<JavaCompile> {
        val targetJavaVersion = 17
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release = targetJavaVersion
        }
    }
}

loom {
    splitEnvironmentSourceSets()
    mods {
        register("packetevents") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.maybeCreate("client"))
        }
    }
    accessWidenerPath = sourceSets.main.get().resources.srcDirs.single()
        .resolve("${rootProject.name}.accesswidener")
}


tasks.compileJava {
    options.release.set(17)
}