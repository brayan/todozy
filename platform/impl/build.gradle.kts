plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = BuildVersion.compileSdk
    namespace = "br.com.sailboat.todozy.platform.impl"

    defaultConfig {
        minSdk = BuildVersion.minSdk

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform(Firebase.bom))
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))

    implementation(AndroidX.appcompat)
    implementation(AndroidX.materialDesign)
    implementation(Timber.timber)
    implementation(Koin.android)
    implementation(Firebase.core)
    implementation(Firebase.crashlytics)
    implementation(Firebase.analytics)

    testImplementation(Junit.junit)

    androidTestImplementation(AndroidXTest.espresso)
}
