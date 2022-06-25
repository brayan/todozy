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
    implementation(project(Module.uiComponentPublic))
    implementation(project(Module.uiComponentImpl))
    implementation(project(Module.domain))
    implementation(project(Module.navigationPublicAndroid))
    implementation(project(Module.alarmPublic))
    implementation(project(Module.settingsPublic))

    implementation(Kotlin.stdlib)
    implementation(Kotlin.reflect)
    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.liveData)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.ktx)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.materialDesign)
    implementation(AndroidX.legacy)
}
