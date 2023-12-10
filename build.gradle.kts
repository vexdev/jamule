plugins {
    signing
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    `java-library`
}

println("Version is $version")

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("org.slf4j:slf4j-api:2.0.9")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.0")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}

kotlin {
    jvmToolchain(11)
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

group = "com.vexdev"

signing {
    val signingKey = providers
        .environmentVariable("GPG_SIGNING_KEY")
        .forUseAtConfigurationTime()
    val signingPassphrase = providers
        .environmentVariable("GPG_SIGNING_PASSPHRASE")
        .forUseAtConfigurationTime()
    if (signingKey.isPresent && signingPassphrase.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
        val extension = extensions
            .getByName("publishing") as PublishingExtension
        sign(extension.publications)
    }
}

object Meta {
    const val desc = "jAmule is a Java client for Amule, a peer to peer file sharing network."
    const val license = "The MIT License"
    const val licenseUrl = "https://opensource.org/license/mit/"
    const val githubRepo = "vexdev/jamule"
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set(project.name)
                description = Meta.desc
                url = "https://github.com/vexdev/${Meta.githubRepo}"
                licenses {
                    license {
                        name = Meta.license
                        url = Meta.licenseUrl
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
                    connection = "scm:git:git@github.com:${Meta.githubRepo}.git"
                    url = "https://github.com/${Meta.githubRepo}.git"
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype()
    }
}