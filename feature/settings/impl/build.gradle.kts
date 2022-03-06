plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = BuildVersion.compileSdk

    defaultConfig {
        minSdk = BuildVersion.minSdk
        targetSdk = BuildVersion.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))
    implementation(project(Module.uiComponent))
    implementation(project(Module.domain))
    implementation(project(Module.settingsPublic))
    implementation(project(Module.alarmPublic))
    implementation(project(Module.aboutPublic))

    implementation(Kotlin.stdlib)
    implementation(Kotlin.reflect)
    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.liveData)
    implementation(Koin.android)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.ktx)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.materialDesign)
    implementation(AndroidX.legacy)
    implementation(Firebase.core)

    testImplementation(Junit.junit)
    testImplementation(MockK.core)
    testImplementation(Kotlin.test)
    testImplementation(Coroutines.test)
    testImplementation(Lifecycle.test)

    androidTestImplementation(MockK.android)
    androidTestImplementation(AndroidXTest.espresso)
    androidTestImplementation(AndroidXTest.runner)
    androidTestImplementation(AndroidXTest.rules)
}