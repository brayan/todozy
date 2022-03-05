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

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.fabric.io/public") }
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
