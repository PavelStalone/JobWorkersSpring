plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

group = "org.example.event"
version = "unspecified"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}
