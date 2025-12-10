plugins {
    id("todozy.android.library.compose")
}

android {
    namespace = "br.com.sailboat.uicomponent.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.utility.kotlinUtil)
    implementation(projects.utility.androidUtil)
    implementation(projects.uiComponent.public)
    implementation(projects.domain)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.material)
    implementation(libs.junit4)
    implementation(libs.coroutines.test)
    implementation(libs.koin.android)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.lifecycle.runtime)
    testImplementation(libs.androidx.compose.ui.test.junit4)

    testImplementation(libs.junit4)

    androidTestImplementation(libs.espresso.core)
}
