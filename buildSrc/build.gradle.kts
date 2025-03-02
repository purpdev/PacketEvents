plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        jvmToolchain(8)
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation(libs.shadow)
    compileOnly(libs.jetbrains.annotations)
    implementation(libs.via.nbt)
    implementation(libs.fast.util)
    implementation(libs.gson)
    implementation(libs.java.diff.utils)
}
