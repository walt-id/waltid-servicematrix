import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "id.walt.servicematrix"
version = "1.1.3"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")

    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")

    // Configuration
    implementation("com.sksamuel.hoplite:hoplite-core:2.6.2")
    implementation("com.sksamuel.hoplite:hoplite-hocon:2.6.2")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("waltid-servicematrix")
                description.set("Kotlin/Java library for service registration.")
                url.set("https://walt.id")
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.walt.id/repository/waltid/")

            val usernameFile = File("secret_maven_username.txt")
            val passwordFile = File("secret_maven_password.txt")
            val secretMavenUsername = System.getenv()["MAVEN_USERNAME"] ?: if (usernameFile.isFile) {
                usernameFile.readLines()[0]
            } else {
                ""
            }
            val secretMavenPassword = System.getenv()["MAVEN_PASSWORD"] ?: if (passwordFile.isFile) {
                passwordFile.readLines()[0]
            } else {
                ""
            }

            credentials {
                username = secretMavenUsername
                password = secretMavenPassword
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

jacoco.toolVersion = "0.8.8"

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}
