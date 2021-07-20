plugins {
    jacoco
    kotlin("jvm") version "1.5.20"
    `maven-publish`
}

group = "id.walt.servicematrix"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))

    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.20")

    // Configuration
    implementation("com.sksamuel.hoplite:hoplite-core:1.4.3")
    implementation("com.sksamuel.hoplite:hoplite-hocon:1.4.3")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5:4.6.0")
    testImplementation("io.kotest:kotest-assertions-core:4.6.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("Walt.ID Service-Matrix")
                description.set("Kotlin/Java library for service registration.")
                url.set("https://walt.id")
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.letstrust.io/repository/waltid/")

            credentials {
                username = "letstrust-build"
                password = "naidohTeiraG9ouzoo0"
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}

jacoco.toolVersion = "0.8.7"

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
    }
}
