plugins {
    id("todozy.android.library")
}

android {
    namespace = "br.com.sailboat.todozy.utility.android"
}

dependencies {
    implementation(projects.utility.kotlinUtil)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.koin.android)

    testImplementation(libs.junit4)

    androidTestImplementation(libs.espresso.core)
}
