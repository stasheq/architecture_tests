plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Config.targetSDK
    buildToolsVersion = Config.buildTools

    defaultConfig {
        applicationId = "me.szymanski.arch"
        minSdk = Config.minSDK
        targetSdk = Config.targetSDK
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        fun buildType(
            name: String,
            minify: Boolean = true,
            proguard: Boolean = true,
            appNameRes: String,
            idSuffix: String? = null,
            versionSuffix: String? = null
        ) = maybeCreate(name).apply {
            isMinifyEnabled = minify
            if (proguard) proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", appNameRes)
            if (idSuffix != null) applicationIdSuffix = ".$idSuffix"
            if (versionSuffix != null) versionNameSuffix = " $versionSuffix"
        }

        buildType(
            name = "debug",
            minify = false,
            proguard = false,
            appNameRes = "@string/app_name_debug",
            idSuffix = "debug",
            versionSuffix = "debug"
        )

        buildType(
            name = "release",
            appNameRes = "@string/app_name_release"
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    debugImplementation(Deps.LeakCanary.lib)
    implementation(project(":logic"))
    implementation(project(":views"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Jetpack.coreKotlin)
    implementation(Deps.Jetpack.appCompat)
    implementation(Deps.Jetpack.constraintLayout)
    implementation(Deps.Jetpack.swipeRefreshLayout)
    implementation(Deps.Jetpack.recyclerView)
    implementation(Deps.MaterialComponents.lib)
    implementation(Deps.Ktx.activity)
    implementation(Deps.Ktx.fragment)
    implementation(Deps.Ktx.Lifecycle.viewModel)
    implementation(Deps.Ktx.Lifecycle.runtime)
    implementation(Deps.OkHttp.lib)
    implementation(Deps.Dagger.lib)
    implementation(Deps.Dagger.Hilt.lib)
    kapt(Deps.Dagger.Hilt.kapt)
}
