plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(Config.targetSDK)
    buildToolsVersion(Config.buildTools)

    defaultConfig {
        minSdkVersion(Config.minSDK)
        targetSdkVersion(Config.targetSDK)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        fun buildType(
            name: String,
            minify: Boolean = true,
            proguard: Boolean = true
        ) = maybeCreate(name).apply {
            isMinifyEnabled = minify
            if (proguard) proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        buildType(
            name = "debug",
            minify = false,
            proguard = false
        )

        buildType(
            name = "release"
        )
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
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Jetpack.coreKotlin)
    implementation(Deps.RxJava.lib)
    implementation(Deps.RxJava.rxAndroid)
    implementation(Deps.RxJava.rxRelay)
    implementation(Deps.Jetpack.constraintLayout)
    implementation(Deps.Jetpack.swipeRefreshLayout)
    implementation(Deps.Jetpack.recyclerView)
    implementation(Deps.MaterialComponents.lib)
}
