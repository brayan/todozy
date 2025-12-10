import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.firebase.crashlytics")
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun loadLocalProperties(): Properties =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

fun hasSigningProps(localProps: Properties): Boolean =
    listOf("STORE_FILE", "STORE_PASSWORD", "KEY_ALIAS", "KEY_PASSWORD")
        .all { key -> localProps.getProperty(key).isNullOrBlank().not() }

extensions.configure<ApplicationExtension> {
    val localProps = loadLocalProperties()

    signingConfigs {
        if (hasSigningProps(localProps)) {
            create("config") {
                keyAlias = localProps.getProperty("KEY_ALIAS")
                keyPassword = localProps.getProperty("KEY_PASSWORD")
                storeFile = file(localProps.getProperty("STORE_FILE"))
                storePassword = localProps.getProperty("STORE_PASSWORD")
            }
        }
    }

    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
    namespace = "br.com.sailboat.todozy"
    defaultConfig {
        applicationId = "br.com.sailboat.todozy"
        minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
        targetSdk = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
        versionCode = 14
        versionName = "1.5"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            signingConfig =
                if (hasSigningProps(localProps)) {
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
                if (hasSigningProps(localProps)) {
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
        resources.excludes += listOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/NOTICE.txt",
            "META-INF/notice.txt",
            "META-INF/ASL2.0",
            "META-INF/*.kotlin_module",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().requiredVersion
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}
