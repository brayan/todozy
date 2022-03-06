object BuildPlugin {
    object Version {
        const val gradlePlugin = "7.1.2"
        const val kotlin = "1.6.10"
        const val googleServices = "4.3.10"
        const val crashlytics = "2.8.1"
    }

    const val android = "com.android.tools.build:gradle:${Version.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    const val googleServices = "com.google.gms:google-services:${Version.googleServices}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Version.crashlytics}"
}

object BuildVersion {
    const val versionCode = 14
    const val versionName = "1.5"
    const val compileSdk = 32
    const val minSdk = 21
    const val targetSdk = 32
    const val buildTools = "30.0.3"
}

object Coroutines {
    object Version {
        const val coroutines = "1.6.0"
    }

    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}"
}

object Lifecycle {
    object Version {
        const val lifecycle = "2.4.1"
        const val lifecycleTesting = "2.1.0"
    }

    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val service = "androidx.lifecycle:lifecycle-service:${Version.lifecycle}"
    const val test = "androidx.arch.core:core-testing:${Version.lifecycleTesting}"
}

object AndroidX {
    object Versions {
        const val ktx = "1.7.0"
        const val appcompat = "1.4.1"
        const val materialDesign = "1.5.0"
        const val browser = "1.4.0"
        const val recyclerView = "1.2.1"
        const val legacy = "1.0.0"
        const val swipeRefreshLayout = "1.1.0"
        const val transition = "1.3.1"
        const val annotation = "1.1.0"
        const val fragment = "1.3.6"
        const val viewPager = "1.0.0"
        const val viewPager2 = "1.0.0"
        const val preferences = "1.1.1"
        const val collection = "1.1.0"
        const val activity = "1.4.0"
        const val constraintLayout = "2.1.3"
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
        const val test = "1.4.0"
        const val junit = "1.1.1"
        const val espresso = "3.4.0"
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
        const val mockk = "1.12.0"
    }

    const val core = "io.mockk:mockk:${Version.mockk}"
    const val android = "io.mockk:mockk-android:${Version.mockk}"
}

object Koin {
    object Version {
        const val android = "3.0.2"
    }

    const val android = "io.insert-koin:koin-android:${Version.android}"
}

object Junit {
    object Version {
        const val junit = "4.13.2"
    }

    const val junit = "junit:junit:${Version.junit}"
}

object Firebase {
    object Version {
        const val core = "20.1.0"
        const val bom = "26.0.0"
    }

    const val core = "com.google.firebase:firebase-core:${Version.core}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val bom = "com.google.firebase:firebase-bom:${Version.bom}"
}

object Kotlin {
    object Version {
        const val kotlin = "1.6.10"
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