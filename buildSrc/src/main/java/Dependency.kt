object BuildPlugin {
    object Version {
        const val gradlePlugin = "8.10.1"
        const val kotlin = "1.9.22"
        const val googleServices = "4.4.2"
        const val crashlytics = "2.9.9"
    }

    const val android = "com.android.tools.build:gradle:${Version.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    const val googleServices = "com.google.gms:google-services:${Version.googleServices}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Version.crashlytics}"
}

object BuildVersion {
    const val versionCode = 14
    const val versionName = "1.5"
    const val compileSdk = 36
    const val minSdk = 24
    const val targetSdk = 35
}

object Coroutines {
    object Version {
        const val coroutines = "1.8.1"
    }

    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}"
}

object Lifecycle {
    object Version {
        const val lifecycle = "2.7.0"
        const val lifecycleTesting = "2.2.0"
    }

    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val service = "androidx.lifecycle:lifecycle-service:${Version.lifecycle}"
    const val test = "androidx.arch.core:core-testing:${Version.lifecycleTesting}"
}

object AndroidX {
    object Versions {
        const val ktx = "1.12.0"
        const val appcompat = "1.6.1"
        const val materialDesign = "1.12.0"
        const val browser = "1.8.0"
        const val recyclerView = "1.3.2"
        const val legacy = "1.0.0"
        const val swipeRefreshLayout = "1.1.0"
        const val transition = "1.4.1"
        const val annotation = "1.7.1"
        const val fragment = "1.7.1"
        const val viewPager = "1.0.0"
        const val viewPager2 = "1.0.0"
        const val preferences = "1.2.1"
        const val collection = "1.4.0"
        const val activity = "1.9.0"
        const val constraintLayout = "2.1.4"
    }

    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
    const val annotations = "androidx.annotation:annotation:${Versions.annotation}"

    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val preferences = "androidx.preference:preference:${Versions.preferences}"
    const val viewPager = "androidx.viewpager:viewpager:${Versions.viewPager}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"

    const val collection = "androidx.collection:collection:${Versions.collection}"
    const val activity = "androidx.activity:activity:${Versions.activity}"
}

object AndroidXTest {
    object Version {
        const val test = "1.5.0"
        const val junit = "1.1.5"
        const val espresso = "3.5.1"
    }

    const val ext = "androidx.test.ext:junit:${Version.junit}"
    const val rules = "androidx.test:rules:${Version.test}"
    const val runner = "androidx.test:runner:${Version.test}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Version.espresso}"
    const val orchestrator = "androidx.test:orchestrator:${Version.test}"
}

object MockK {
    object Version {
        const val mockk = "1.13.10"
    }

    const val core = "io.mockk:mockk:${Version.mockk}"
    const val android = "io.mockk:mockk-android:${Version.mockk}"
}

object Koin {
    object Version {
        const val koin = "3.5.6"
    }

    const val android = "io.insert-koin:koin-android:${Version.koin}"
    const val test = "io.insert-koin:koin-test:${Version.koin}"
}

object Junit {
    object Version {
        const val junit = "4.13.2"
    }

    const val junit = "junit:junit:${Version.junit}"
}

object Firebase {
    object Version {
        const val core = "21.1.1"
        const val bom = "32.8.1"
    }

    const val core = "com.google.firebase:firebase-core:${Version.core}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val bom = "com.google.firebase:firebase-bom:${Version.bom}"
}

object Kotlin {
    object Version {
        const val kotlin = "1.9.22"
    }

    const val test = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
}

object Timber {
    object Version {
        const val timber = "4.7.1"
    }

    const val timber = "com.jakewharton.timber:timber:${Version.timber}"
}

object Flipper {
    object Version {
        const val flipper = "0.154.0"
        const val soloader = "0.10.1"
    }

    const val flipper = "com.facebook.flipper:flipper:${Version.flipper}"
    const val flipperNoop = "com.facebook.flipper:flipper-noop:${Version.flipper}"
    const val soloader = "com.facebook.soloader:soloader:${Version.soloader}"
}

object Hilt {
    object Version {
        const val core = "2.51.1"
        const val lifecycleViewModel = "1.1.0"
        const val compiler = "1.1.0"
        const val navigationCompose = "1.2.0"
    }

    const val android = "com.google.dagger:hilt-android:${Version.core}"
    const val androidCompiler = "com.google.dagger:hilt-android-compiler:${Version.core}"
    const val lifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Version.lifecycleViewModel}"
    const val compiler = "androidx.hilt:hilt-compiler:${Version.compiler}"
    const val navigationCompose = "androidx.hilt:hilt-navigation-compose:${Version.navigationCompose}"
}

object Compose {
    object Version {
        const val core = "1.6.7"
        const val lifecycleRuntime = "2.7.0"
        const val activity = "1.9.0"
        const val compiler = "1.5.10"
    }

    const val ui = "androidx.compose.ui:ui:${Version.core}"
    const val material = "androidx.compose.material:material:${Version.core}"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Version.core}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleRuntime}"
    const val activity = "androidx.activity:activity-compose:${Version.activity}"
}

object Desugar {
    object Version {
        const val jdkLibs = "2.0.4"
    }

    const val jdkLibs = "com.android.tools:desugar_jdk_libs:${Version.jdkLibs}"
}
