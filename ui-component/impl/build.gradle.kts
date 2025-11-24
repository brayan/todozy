plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = BuildVersion.compileSdk
    namespace = "br.com.sailboat.uicomponent.impl"

    defaultConfig {
        minSdk = BuildVersion.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))
    implementation(project(Module.uiComponentPublic))

    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.materialDesign)
    implementation(Junit.junit)
    implementation(Coroutines.test)
    implementation(Koin.android)

    testImplementation(Junit.junit)

    androidTestImplementation(AndroidXTest.espresso)
}
