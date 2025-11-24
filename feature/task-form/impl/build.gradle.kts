plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = BuildVersion.compileSdk
    namespace = "br.com.sailboat.todozy.feature.task.form.impl"

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
    implementation(project(Module.uiComponentImpl))
    implementation(project(Module.domain))
    implementation(project(Module.navigationPublicAndroid))
    implementation(project(Module.taskFormPublic))
    implementation(project(Module.taskDetailsPublic))
    implementation(project(Module.alarmPublic))

    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.liveData)
    implementation(Koin.android)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.materialDesign)

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
