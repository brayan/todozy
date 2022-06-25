plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = BuildVersion.compileSdk
    buildToolsVersion = BuildVersion.buildTools

    defaultConfig {
        applicationId = "br.com.sailboat.todozy"
        minSdk = BuildVersion.minSdk
        targetSdk = BuildVersion.targetSdk
        versionCode = BuildVersion.versionCode
        versionName = BuildVersion.versionName
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }

        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true
        }
    }
    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/*.kotlin_module")
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Module.kotlinUtil))
    implementation(project(Module.androidUtil))
    implementation(project(Module.uiComponentPublic))
    implementation(project(Module.uiComponentImpl))
    implementation(project(Module.domain))
    implementation(project(Module.navigationPublicAndroid))
    implementation(project(Module.aboutImpl))
    implementation(project(Module.settingsPublic))
    implementation(project(Module.settingsImpl))
    implementation(project(Module.alarmPublic))
    implementation(project(Module.alarmImpl))
    implementation(project(Module.taskFormPublic))
    implementation(project(Module.taskFormImpl))
    implementation(project(Module.taskHistoryPublic))
    implementation(project(Module.taskHistoryImpl))
    implementation(project(Module.taskDetailsPublic))
    implementation(project(Module.taskDetailsImpl))
    implementation(project(Module.taskListPublic))
    implementation(project(Module.taskListImpl))
    implementation(project(Module.splashImpl))
    implementation(project(Module.platformImpl))

    implementation(Kotlin.stdlib)
    implementation(Kotlin.reflect)
    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.liveData)
    implementation(Koin.android)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.ktx)
    implementation(Timber.timber)

    testImplementation(Junit.junit)
    testImplementation(MockK.core)
    testImplementation(Kotlin.test)
    testImplementation(Coroutines.test)
    testImplementation(Lifecycle.test)
    testImplementation(Koin.test)
}
