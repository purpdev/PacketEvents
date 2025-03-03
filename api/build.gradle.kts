import com.github.retrooper.compression.strategy.JsonArrayCompressionStrategy
import com.github.retrooper.compression.strategy.JsonObjectCompressionStrategy
import com.github.retrooper.compression.strategy.JsonToNbtStrategy
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

    // Minecraft jar input for generator
    //compileOnly(files("C:/Users/User/Documents/newwind_source/newwind_plugins/NWNMS/libs/paper-1.21.4.jar"))
    //compileOnly("com.mojang:datafixerupper:8.0.16")
    //compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

mappingCompression {
    mappingDirectory = rootDir.resolve("mappings")
    outDirectory = project.layout.buildDirectory.dir("mappings/generated/assets/mappings")

    with<JsonToNbtStrategy> {
        compress("block/legacy_block_mappings.json")
        compress("block/modern_block_mappings.json")

        compress("enchantment/enchantment_type_data.json")

        compress("item/item_base_components.json")

        compress("stats/statistics.json")

        compress("world/biome_data.json")
    }

    with<JsonArrayCompressionStrategy> {
        compress("attribute/attribute_mappings.json")

        compress("block/block_entity_type_mappings.json")
        compress("block/block_type_mappings.json")

        compress("chat/chat_type_mappings.json")

        compress("command/argument_parser_mappings.json")

        compress("damage/damagetype_mappings.json")

        compress("enchantment/effect_component_type.json")

        compress("entity/entity_data_type_mappings.json")
        compress("entity/painting_mappings.json")
        compress("entity/wolf_variant_mappings.json")

        compress("item/consume_effect_type_mappings.json")
        compress("item/equipment_asset_mappings.json")
        compress("item/item_banner_pattern_mappings.json")
        compress("item/item_component_mappings.json")
        compress("item/item_instrument_mappings.json")
        compress("item/item_jukebox_song_mappings.json")
        compress("item/item_map_decoration_type_mappings.json")
        compress("item/item_potion_mappings.json")
        compress("item/item_trim_material_mappings.json")
        compress("item/item_trim_pattern_mappings.json")
        compress("item/recipe_book_category.json")
        compress("item/recipe_display_types.json")
        compress("item/recipe_serializer_mappings.json")
        compress("item/recipe_slot_display_types.json")

        compress("particle/particle_type_mappings.json")

        compress("sound/sound_mappings.json")

        compress("world/biome_mappings.json")
        compress("world/world_position_source_mappings.json")
    }

    with<JsonObjectCompressionStrategy> {
        compress("enchantment/enchantment_type_mappings.json")

        compress("entity/entity_effect_mappings.json")
        compress("entity/entity_type_mappings.json")
        compress("entity/legacy_entity_type_mappings.json")

        compress("item/item_type_mappings.json")

        compress("world/dimension_type_mappings.json")
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
            path.startsWith("net/kyori") && !path.startsWith("net/kyori/adventure/text/serializer") && !path.startsWith("net/kyori/option")
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
