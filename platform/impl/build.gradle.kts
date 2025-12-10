plugins {
    id("todozy.android.library.crashlytics")
}

android {
    namespace = "br.com.sailboat.todozy.platform.impl"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(projects.utility.kotlinUtil)
    implementation(projects.utility.androidUtil)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.timber)
    implementation(libs.koin.android)
    implementation(libs.firebase.core)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    testImplementation(libs.junit4)

    androidTestImplementation(libs.espresso.core)
}
