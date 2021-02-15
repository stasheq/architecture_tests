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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.Kotlin.version}")
    implementation("androidx.core:core-ktx:${Deps.coreKtx}")
    implementation("io.reactivex.rxjava3:rxandroid:${Deps.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Deps.swipeRefreshLayout}")
    implementation("androidx.recyclerview:recyclerview:${Deps.recyclerView}")
    implementation("com.google.android.material:material:${Deps.material}")
}
