import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    kotlin("android")
    kotlin("kapt")
}

val localProps =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

val hasSigningProps =
    listOf("STORE_FILE", "STORE_PASSWORD", "KEY_ALIAS", "KEY_PASSWORD")
        .all { key -> localProps.getProperty(key).isNullOrBlank().not() }

android {
    signingConfigs {
        if (hasSigningProps) {
            create("config") {
                keyAlias = localProps.getProperty("KEY_ALIAS")
                keyPassword = localProps.getProperty("KEY_PASSWORD")
                storeFile = file(localProps.getProperty("STORE_FILE"))
                storePassword = localProps.getProperty("STORE_PASSWORD")
            }
        }
    }

    compileSdk = BuildVersion.compileSdk
    namespace = "br.com.sailboat.todozy"

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
            signingConfig =
                if (hasSigningProps) {
                    signingConfigs.getByName("config")
                } else {
                    signingConfigs.getByName("debug")
                }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isDebuggable = false
        }

        getByName("debug") {
            signingConfig =
                if (hasSigningProps) {
                    signingConfigs.getByName("config")
                } else {
                    signingConfigs.getByName("debug")
                }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isDebuggable = true
        }
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Module.aboutImpl))
    implementation(project(Module.settingsImpl))
    implementation(project(Module.alarmImpl))
    implementation(project(Module.taskFormImpl))
    implementation(project(Module.taskHistoryImpl))
    implementation(project(Module.taskDetailsImpl))
    implementation(project(Module.taskListImpl))
    implementation(project(Module.splashImpl))
    implementation(project(Module.platformImpl))
    implementation(project(Module.uiComponentImpl))

    implementation(Koin.android)
    implementation(Timber.timber)

    testImplementation(Junit.junit)
    testImplementation(MockK.core)
    testImplementation(Koin.test)
}
