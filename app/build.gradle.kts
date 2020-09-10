plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "me.szymanski.listtest"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = (project.properties["BUILD_NUMBER"] as String).toInt()
        versionName = project.properties["VERSION_NAME"] as String
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        maybeCreate("release").apply {
            // command line signing config set on CI machine
            storeFile = file(project.properties["SIGN_STORE_FILE"] as String)
            storePassword = project.properties["SIGN_STORE_PASSWORD"] as String
            keyAlias = project.properties["SIGN_KEY_ALIAS"] as String
            keyPassword = project.properties["SIGN_KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        fun buildType(
            name: String,
            minify: Boolean = true,
            proguard: Boolean = true,
            commandLineSigning: Boolean = true,
            appNameRes: String,
            idSuffix: String? = null,
            versionSuffix: String? = null
        ) = maybeCreate(name).apply {
            isMinifyEnabled = minify
            if (proguard) proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", appNameRes)
            if (commandLineSigning) signingConfig = signingConfigs["release"]
            if (idSuffix != null) applicationIdSuffix = ".$idSuffix"
            if (versionSuffix != null) versionNameSuffix = " $versionSuffix"
        }

        buildType(
            name = "debug",
            minify = false,
            proguard = false,
            appNameRes = "@string/app_name_debug",
            idSuffix = "debug",
            versionSuffix = "debug",
            commandLineSigning = false
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
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")
    implementation(project(":logic"))
    implementation(project(":glueAndroid"))
    implementation(project(":glueKotlin"))
    implementation(project(":widgets"))
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
    implementation("io.reactivex.rxjava3:rxandroid:${Deps.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("com.google.dagger:dagger:${Deps.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Deps.dagger}")
    testImplementation("junit:junit:${Deps.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Deps.junitAndroid}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Deps.espresso}")
}
