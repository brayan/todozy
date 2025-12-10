plugins {
    id("todozy.android.library")
}

android {
    namespace = "br.com.sailboat.todozy.feature.splash.impl"
}

dependencies {
    implementation(projects.utility.androidUtil)
    implementation(projects.feature.navigation.publicAndroid)
    implementation(projects.feature.settings.public)
    implementation(projects.uiComponent.impl)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.koin.android)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.lifecycle.test)
}
