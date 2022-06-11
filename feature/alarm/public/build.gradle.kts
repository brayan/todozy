plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.uiComponentPublic))
    implementation(project(Module.domain))
}
