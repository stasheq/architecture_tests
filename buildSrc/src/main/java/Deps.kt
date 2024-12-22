object Deps {
    object Kotlin {
        // https://kotlinlang.org/
        const val version = "2.0.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"

        // https://github.com/Kotlin/kotlinx.coroutines
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0"
    }

    object Jetpack {
        // https://developer.android.com/jetpack/androidx/releases/core
        const val coreKotlin = "androidx.core:core-ktx:1.13.1"

        // https://developer.android.com/jetpack/androidx/releases/appcompat
        const val appCompat = "androidx.appcompat:appcompat:1.7.0"

        // https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"

        // https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

        // https://developer.android.com/jetpack/androidx/releases/recyclerview
        const val recyclerView = "androidx.recyclerview:recyclerview:1.3.2"

        // https://developer.android.com/jetpack/androidx/releases/navigation
        const val composeNavigation = "androidx.navigation:navigation-compose:2.8.3"

        // https://developer.android.com/jetpack/androidx/releases/activity
        const val composeActivity = "androidx.activity:activity-compose:1.9.3"

        // https://developer.android.com/jetpack/androidx/releases/compose-ui
        const val composeUi = "androidx.compose.ui:ui:1.7.5"

        // https://developer.android.com/jetpack/androidx/releases/compose-foundation
        const val composeFoundation = "androidx.compose.foundation:foundation:1.7.5"

        // https://developer.android.com/jetpack/androidx/releases/compose-material3
        const val composeMaterial3 = "androidx.compose.material3:material3:1.3.1"

        // https://developer.android.com/jetpack/androidx/releases/hilt
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.2.0"
    }

    object Ktx {
        // https://developer.android.com/kotlin/ktx/extensions-list#androidxactivity
        const val activity = "androidx.activity:activity-ktx:1.9.3"

        // https://developer.android.com/kotlin/ktx/extensions-list#androidxfragmentapp
        const val fragment = "androidx.fragment:fragment-ktx:1.8.4"

        object Lifecycle {
            // https://developer.android.com/kotlin/ktx/extensions-list#androidxlifecycle
            private const val version = "2.8.6"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }
    }

    object Dagger {
        // https://github.com/google/dagger
        const val version = "2.51.1"
        const val lib = "com.google.dagger:dagger:$version"
        const val kapt = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val lib = "com.google.dagger:hilt-android:$version"
            const val kapt = "com.google.dagger:hilt-android-compiler:$version"
        }
    }

    object Retrofit {
        // https://github.com/square/retrofit/releases
        private const val version = "2.11.0"
        const val lib = "com.squareup.retrofit2:retrofit:$version"
        const val gson = "com.squareup.retrofit2:converter-gson:$version"
    }

    object OkHttp {
        // https://square.github.io/okhttp/changelog/
        private const val version = "4.12.0"
        const val lib = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockwebserver = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object MaterialComponents {
        // https://github.com/material-components/material-components-android/releases
        const val lib = "com.google.android.material:material:1.12.0"
    }

    object LeakCanary {
        // https://github.com/square/leakcanary/releases
        private const val version = "2.14"
        const val lib = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    // tests
    object Kotest {
        // https://github.com/kotest/kotest/releases
        private const val version = "5.9.1"
        const val lib = "io.kotest:kotest-runner-junit5:$version"
        const val assertions = "io.kotest:kotest-assertions-core:$version"
        const val property = "io.kotest:kotest-property:$version"
    }
}
