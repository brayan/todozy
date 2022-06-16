buildscript {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath(BuildPlugin.android)
        classpath(BuildPlugin.kotlin)
        classpath(BuildPlugin.googleServices)
        classpath(BuildPlugin.crashlytics)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
