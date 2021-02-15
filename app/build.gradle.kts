plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Config.targetSDK)
    buildToolsVersion(Config.buildTools)

    defaultConfig {
        applicationId = "me.szymanski.arch"
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
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Deps.leakCanary}")
    implementation(project(":views"))
    implementation(project(":CoreMultiplatform"))
    implementation(kotlin("stdlib", Deps.Kotlin.version))
    implementation("androidx.core:core-ktx:${Deps.coreKtx}")
    implementation("androidx.activity:activity-ktx:${Deps.activityKtx}")
    implementation("androidx.fragment:fragment-ktx:${Deps.fragmentKtx}")
    implementation("androidx.appcompat:appcompat:${Deps.androidAppCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Deps.swipeRefreshLayout}")
    implementation("androidx.recyclerview:recyclerview:${Deps.recyclerView}")
    implementation("com.google.android.material:material:${Deps.material}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Deps.viewModelKtx}")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Deps.hiltJetpack}")
    implementation("com.squareup.okhttp3:okhttp:${Deps.okhttp}")
    implementation("io.reactivex.rxjava3:rxandroid:${Deps.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation(Deps.koin)
    implementation(Deps.coroutines)
    implementation("com.google.dagger:dagger:${Deps.dagger}")
    implementation("com.google.dagger:hilt-android:${Deps.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Deps.hilt}")
    kapt("androidx.hilt:hilt-compiler:${Deps.hiltJetpack}")
}
