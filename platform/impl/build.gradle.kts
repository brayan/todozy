plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
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
    implementation(platform(Firebase.bom))
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))
    implementation(project(Module.uiComponentPublic))

    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.ktx)
    implementation(AndroidX.materialDesign)
    implementation(AndroidX.legacy)
    implementation(Junit.junit)
    implementation(Kotlin.test)
    implementation(Coroutines.test)
    implementation(Lifecycle.test)
    implementation(Timber.timber)
    implementation(Koin.android)
    implementation(Firebase.core)
    implementation(Firebase.crashlytics)
    implementation(Firebase.analytics)

    testImplementation(Junit.junit)

    androidTestImplementation(AndroidXTest.espresso)
}
