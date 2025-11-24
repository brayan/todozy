plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = BuildVersion.compileSdk
    namespace = "br.com.sailboat.todozy.feature.splash.impl"

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
    implementation(project(Module.androidUtil))
    implementation(project(Module.navigationPublicAndroid))
    implementation(project(Module.settingsPublic))
    implementation(project(Module.uiComponentImpl))

    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.liveData)
    implementation(Koin.android)
    implementation(AndroidX.appcompat)

    testImplementation(Junit.junit)
    testImplementation(MockK.core)
    testImplementation(Kotlin.test)
    testImplementation(Coroutines.test)
    testImplementation(Lifecycle.test)
}
