import com.github.retrooper.compression.strategy.dir.JsonBase64DataDirStrategy
import com.github.retrooper.compression.strategy.dir.JsonRegistryCompressionDirStrategy
import com.github.retrooper.compression.strategy.dir.JsonToNbtDirStrategy
import com.github.retrooper.excludeAdventure

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    `mapping-compression`
    `pe-version`
}

// papermc repo + disableAutoTargetJvm needed for mockbukkit
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    withJavadocJar()
}

dependencies {
    compileOnlyApi(libs.bundles.adventure)
    compileOnlyApi(libs.bundles.adventure.serializers)
    implementation(libs.adventure.api)
    api(project(":patch:adventure-text-serializer-gson", "shadow")) {
        excludeAdventure()
    }
    api(libs.adventure.text.serializer.legacy) {
        excludeAdventure()
    }
    compileOnly(libs.gson)

    testImplementation(libs.bundles.adventure)
    testImplementation(project(":patch:adventure-text-serializer-gson"))
    testImplementation(libs.adventure.text.serializer.legacy)
    testImplementation(project(":netty-common"))
    testImplementation(testlibs.mockbukkit)
    testImplementation(testlibs.slf4j)
    testImplementation(testlibs.bundles.junit)
    testImplementation(libs.netty)
    testImplementation(libs.classgraph)
}

mappingCompression {
    mappingDirectory = rootDir.resolve("mappings")
    outDirectory = project.layout.buildDirectory.dir("mappings/generated/assets/mappings")

    with<JsonToNbtDirStrategy> {
        compress("data")
    }
    with<JsonBase64DataDirStrategy> {
        compress("item_base_components")
    }
    with<JsonRegistryCompressionDirStrategy> {
        compress("registries")
    }
}

tasks {
    assemble {
        setDependsOn(dependsOn.filterNot { it == "shadowNoAdventure" })
    }

    javadoc {
        mustRunAfter(generateVersionsFile)
    }

    sourcesJar {
        mustRunAfter(generateVersionsFile)
    }

    withType<JavaCompile> {
        dependsOn(generateVersionsFile)
    }

    processResources {
        dependsOn(compressMappings)
        from(project.layout.buildDirectory.dir("mappings/generated").get())
    }

    generateVersionsFile {
        packageName = "com.github.retrooper.packetevents.util"
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        exclude {
            val path = it.path
            path.startsWith("net/kyori") && !path.startsWith("net/kyori/adventure/text/serializer") && !path.startsWith(
                "net/kyori/option"
            )
        }
    }
}

publishing {
    publications {
        named<MavenPublication>("shadow") {
            artifact(tasks["javadocJar"])
        }
    }
}
