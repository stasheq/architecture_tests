plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = Config.targetSDK
    buildToolsVersion = Config.buildTools
    namespace = "${Config.appId}.widgets"

    defaultConfig {
        minSdk = Config.minSDK
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":commonTools"))
    implementation(Deps.Jetpack.coreKotlin)
    implementation(Deps.Jetpack.constraintLayout)
    implementation(Deps.Jetpack.swipeRefreshLayout)
    implementation(Deps.Jetpack.recyclerView)
    implementation(Deps.MaterialComponents.lib)
}
