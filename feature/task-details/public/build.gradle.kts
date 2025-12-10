plugins {
    id("todozy.jvm.library")
}

dependencies {
    implementation(projects.utility.kotlinUtil)
    implementation(projects.domain)
    implementation(projects.feature.taskHistory.public)
}
