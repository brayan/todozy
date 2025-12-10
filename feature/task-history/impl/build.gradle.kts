plugins {
    id("todozy.android.library")
}

android {
    namespace = "br.com.sailboat.todozy.feature.task.history.impl"
}

dependencies {
    implementation(projects.utility.kotlinUtil)
    implementation(projects.utility.androidUtil)
    implementation(projects.uiComponent.public)
    implementation(projects.uiComponent.impl)
    implementation(projects.domain)
    implementation(projects.feature.navigation.publicAndroid)
    implementation(projects.feature.taskHistory.public)
    implementation(projects.feature.taskDetails.public)
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.koin.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.material)

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
