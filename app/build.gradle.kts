plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "me.szymanski.arch"
        minSdkVersion(21)
        targetSdkVersion(30)
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
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")
    implementation(project(":logic"))
    implementation(project(":views"))
    implementation(project(":glueKotlin"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlin}")
    implementation("androidx.core:core-ktx:${Deps.androidKtx}")
    implementation("androidx.activity:activity-ktx:${Deps.activityKtx}")
    implementation("androidx.fragment:fragment-ktx:${Deps.fragmentKtx}")
    implementation("androidx.appcompat:appcompat:${Deps.androidAppCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Deps.swipeRefreshLayout}")
    implementation("androidx.recyclerview:recyclerview:${Deps.recyclerView}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Deps.androidLifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Deps.viewModelKtx}")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Deps.lifecycleHilt}")
    implementation("io.reactivex.rxjava3:rxandroid:${Deps.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("com.google.dagger:dagger:${Deps.dagger}")
    implementation("com.google.dagger:hilt-android:${Deps.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Deps.hilt}")
    kapt("androidx.hilt:hilt-compiler:${Deps.lifecycleHilt}")
    testImplementation("junit:junit:${Deps.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Deps.junitAndroid}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Deps.espresso}")
}
