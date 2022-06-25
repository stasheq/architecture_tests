object Deps {
    object Module {
        const val domain = ":domain"
        const val views = ":views"
        const val commonTools = ":commonTools"
        const val restApi = ":restApi"
    }

    object Kotlin {
        // https://kotlinlang.org/
        const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"

        // https://github.com/Kotlin/kotlinx.coroutines
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3"
    }

    object Jetpack {
        // https://developer.android.com/jetpack/androidx/releases/core
        const val coreKotlin = "androidx.core:core-ktx:1.8.0"

        // https://developer.android.com/jetpack/androidx/releases/appcompat
        const val appCompat = "androidx.appcompat:appcompat:1.4.2"

        // https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"

        // https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

        // https://developer.android.com/jetpack/androidx/releases/recyclerview
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
    }

    object Ktx {
        // https://developer.android.com/kotlin/ktx/extensions-list#androidxactivity
        const val activity = "androidx.activity:activity-ktx:1.4.0"

        // https://developer.android.com/kotlin/ktx/extensions-list#androidxfragmentapp
        const val fragment = "androidx.fragment:fragment-ktx:1.4.1"

        object Lifecycle {
            // https://developer.android.com/kotlin/ktx/extensions-list#androidxlifecycle
            private const val version = "2.5.0-rc02"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }
    }

    object Dagger {
        // https://github.com/google/dagger
        const val version = "2.42"
        const val lib = "com.google.dagger:dagger:$version"
        const val kapt = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val lib = "com.google.dagger:hilt-android:$version"
            const val kapt = "com.google.dagger:hilt-android-compiler:$version"
        }
    }

    object Retrofit {
        // https://github.com/square/retrofit/releases
        private const val version = "2.9.0"
        const val lib = "com.squareup.retrofit2:retrofit:$version"
        const val gson = "com.squareup.retrofit2:converter-gson:$version"
    }

    object OkHttp {
        // https://square.github.io/okhttp/changelog/
        private const val version = "4.10.0"
        const val lib = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockwebserver = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object MaterialComponents {
        // https://github.com/material-components/material-components-android/releases
        const val lib = "com.google.android.material:material:1.6.1"
    }

    object LeakCanary {
        // https://github.com/square/leakcanary/releases
        private const val version = "2.9.1"
        const val lib = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    // tests
    object Kotest {
        // https://github.com/kotest/kotest/releases
        private const val version = "5.3.0"
        const val lib = "io.kotest:kotest-runner-junit5:$version"
        const val assertions = "io.kotest:kotest-assertions-core:$version"
        const val property = "io.kotest:kotest-property:$version"
    }
}
