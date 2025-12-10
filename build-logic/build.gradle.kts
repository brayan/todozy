plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    implementation("com.google.firebase:firebase-crashlytics-gradle:${libs.versions.crashlyticsGradle.get()}")
}
