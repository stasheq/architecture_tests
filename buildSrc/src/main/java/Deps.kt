object Deps {
    // https://kotlinlang.org/
    object Kotlin {
        const val version = "1.4.30"
    }


    // https://github.com/Kotlin/kotlinx.coroutines
    private const val coroutinesVersion = "1.4.2"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"

    // https://developer.android.com/kotlin/ktx/extensions-list
    const val coreKtx = "1.3.2"

    // https://developer.android.com/kotlin/ktx/extensions-list#androidxactivity
    const val activityKtx = "1.2.0"

    // https://developer.android.com/kotlin/ktx/extensions-list#androidxfragmentapp
    const val fragmentKtx = "1.2.5"

    // https://developer.android.com/kotlin/ktx/extensions-list#androidxlifecycle
    const val viewModelKtx = "2.2.0"

    // https://github.com/google/dagger
    const val dagger = "2.32"
    const val hilt = "$dagger-alpha"

    // https://developer.android.com/training/dependency-injection/hilt-jetpack
    // https://developer.android.com/jetpack/androidx/releases/hilt
    const val hiltJetpack = "1.0.0-alpha03"

    // https://github.com/square/retrofit/releases
    const val retrofit = "2.9.0"

    // https://square.github.io/okhttp/changelog/
    const val okhttp = "4.9.1"

    // https://developer.android.com/jetpack/androidx/releases/appcompat
    const val androidAppCompat = "1.2.0"

    // https://developer.android.com/jetpack/androidx/releases/constraintlayout
    const val constraintLayout = "2.0.4"

    // https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout
    const val swipeRefreshLayout = "1.1.0"

    // https://developer.android.com/jetpack/androidx/releases/recyclerview
    const val recyclerView = "1.1.0"

    // https://github.com/material-components/material-components-android/releases
    const val material = "1.3.0"

    // https://github.com/ReactiveX/RxJava/releases
    const val rxJava = "3.0.10"

    // https://github.com/JakeWharton/RxRelay/releases
    const val rxRelay = "3.0.0"

    // https://github.com/ReactiveX/RxAndroid/releases
    const val rxAndroid = "3.0.0"

    // https://github.com/square/leakcanary/releases
    const val leakCanary = "2.6"

    // https://github.com/ktorio/ktor/releases
    object Ktor {
        private const val version = "1.5.1"
        const val commonCore = "io.ktor:ktor-client-core:$version"
        const val commonJson = "io.ktor:ktor-client-json:$version"
        const val commonLogging = "io.ktor:ktor-client-logging:$version"
        const val commonSerialization = "io.ktor:ktor-client-serialization:$version"
        const val android = "io.ktor:ktor-client-okhttp:$version"
        const val ios = "io.ktor:ktor-client-ios:$version"
        const val js = "io.ktor:ktor-client-js:$version"
    }

    // tests
    // https://github.com/kotest/kotest/releases
    const val kotest = "4.4.1"
}
