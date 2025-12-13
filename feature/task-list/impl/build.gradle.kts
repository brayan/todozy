plugins {
    id("todozy.android.library.compose")
}

android {
    namespace = "br.com.sailboat.todozy.feature.task.list.impl"

    buildFeatures {
        viewBinding = false
    }
    testOptions {
        unitTests.all {
            it.systemProperty("kotlinx.coroutines.fast.service.loader", "false")
        }
    }
}

configurations.matching { it.name.contains("UnitTest") }.configureEach {
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
}

dependencies {
    implementation(projects.utility.kotlinUtil)
    implementation(projects.utility.androidUtil)
    implementation(projects.uiComponent.public)
    implementation(projects.uiComponent.impl)
    implementation(projects.domain)
    implementation(projects.feature.taskList.public)
    implementation(projects.feature.navigation.publicAndroid)
    implementation(projects.feature.alarm.public)
    implementation(projects.feature.taskDetails.public)
    implementation(projects.feature.taskForm.public)
    implementation(projects.feature.taskHistory.public)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.compose.lifecycle.runtime.compose)
    implementation(libs.koin.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.lifecycle.test)
    testRuntimeOnly(libs.coroutines.test)

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
}
