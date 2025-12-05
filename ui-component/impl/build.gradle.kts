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
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.Version.compiler
    }
}

dependencies {
    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))
    implementation(project(Module.uiComponentPublic))
    implementation(project(Module.domain))

    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.materialDesign)
    implementation(Junit.junit)
    implementation(Coroutines.test)
    implementation(Koin.android)
    implementation(Compose.ui)
    implementation(Compose.material)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.lifecycleRuntimeKtx)
    testImplementation(Compose.uiTestJunit4)

    testImplementation(Junit.junit)

    androidTestImplementation(AndroidXTest.espresso)
}
