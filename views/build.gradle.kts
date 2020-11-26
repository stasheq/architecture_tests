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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlin}")
    implementation("androidx.core:core-ktx:${Deps.androidKtx}")
    implementation("io.reactivex.rxjava3:rxandroid:${Deps.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("com.jakewharton.rxbinding4:rxbinding-core:${Deps.rxBinding}")
    implementation("com.jakewharton.rxbinding4:rxbinding-material:${Deps.rxBinding}")
    implementation("com.jakewharton.rxbinding4:rxbinding-recyclerview:${Deps.rxBinding}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayout}")
    testImplementation("junit:junit:${Deps.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Deps.junitAndroid}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Deps.espresso}")
}
