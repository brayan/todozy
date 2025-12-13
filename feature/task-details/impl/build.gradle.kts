plugins {
    id("todozy.android.library.compose")
}

android {
    namespace = "br.com.sailboat.todozy.feature.task.details.impl"
}

dependencies {
    implementation(projects.utility.kotlinUtil)
    implementation(projects.utility.androidUtil)
    implementation(projects.uiComponent.public)
    implementation(projects.uiComponent.impl)
    implementation(projects.domain)
    implementation(projects.feature.navigation.publicAndroid)
    implementation(projects.feature.alarm.public)
    implementation(projects.feature.taskHistory.public)
    implementation(projects.feature.taskDetails.public)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.koin.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.lifecycle.test)

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
}

configurations
    .matching { it.name.contains("UnitTest") }
    .configureEach {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }
