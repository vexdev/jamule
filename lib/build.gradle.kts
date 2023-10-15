import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    `java-library`
    `maven-publish`
}

group = "com.vexdev"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:32.1.1-jre")
    implementation("org.slf4j:slf4j-api:2.0.9")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation("ch.qos.logback:logback-classic:1.4.11")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.7.2")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "jamule"
            from(components["java"])
            pom {
                name = "jAmule"
                description = "jAmule is a Java client for Amule, a peer to peer file sharing network."
                url = "https://github.com/vexdev/jamule"
                licenses {
                    license {
                        // MIT license
                        name = "The MIT License"
                        url = "https://opensource.org/license/mit/"
                    }
                }
                developers {
                    developer {
                        id = "vexdev"
                        name = "Luca Vitucci"
                        email = "luca@vexdev.com"
                    }
                }
                scm {
                    connection = "scm:git:git@github.com:vexdev/jamule.git"
                    url = "https://github.com/vexdev/jamule"
                }
            }
        }
    }
}

tasks.withType<ProcessResources> {
    doLast {
        val propertiesFile = file("$buildDir/resources/main/build.properties")
        val propertiesFileTest = file("$buildDir/resources/test/build.properties")
        propertiesFile.parentFile.mkdirs()
        propertiesFileTest.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", rootProject.version.toString())
        propertiesFile.writer().use { properties.store(it, null) }
        propertiesFileTest.writer().use { properties.store(it, null) }
    }
}