import com.google.protobuf.gradle.id
import org.gradle.kotlin.dsl.getByName
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.5"
}

group = "org.example.grpc"
version = "unspecified"

repositories {
    mavenCentral()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
tasks.getByName("jar") {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("io.grpc:grpc-netty-shaded:1.77.0")
    api("io.grpc:grpc-protobuf:1.77.0")
    api("io.grpc:grpc-stub:1.77.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.8"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.77.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("@generated=omit")
                }
            }
        }
    }
}
