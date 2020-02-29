
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    publishing
}

group = "growatt"
version = "1.0.0"

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

bintray {
    user = System.getenv("bintrayUser")
    key = System.getenv("bintrayApiKey")
    setPublications("mavenJava")
    with(pkg) {
        repo = "maven"
        name = "growatt-api"
        desc = "Growatt API written for the JVM in Kotlin"
        userOrg = "szantogab"
        setLicenses("MIT")
        vcsUrl = "https://github.com/szantogab/growatt4j.git"
        with(version) {
            name = project.version.toString()
        }
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io") {
        name = "jitpack"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.2.1")
    implementation("com.github.kittinunf.fuel:fuel-gson:2.2.1")
    implementation("com.google.code.gson:gson:2.8.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
