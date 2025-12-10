plugins {
    id("todozy.android.application")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(projects.feature.about.impl)
    implementation(projects.feature.settings.impl)
    implementation(projects.feature.alarm.impl)
    implementation(projects.feature.taskForm.impl)
    implementation(projects.feature.taskHistory.impl)
    implementation(projects.feature.taskDetails.impl)
    implementation(projects.feature.taskList.impl)
    implementation(projects.feature.splash.impl)
    implementation(projects.platform.impl)
    implementation(projects.uiComponent.impl)
    implementation(projects.feature.navigation.publicAndroid)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.koin.android)
    implementation(libs.timber)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk.core)
    testImplementation(libs.koin.test)
}
