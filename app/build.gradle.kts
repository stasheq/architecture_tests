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
}

dependencies {
    implementation(project(":logic"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("com.google.dagger:dagger:${Deps.daggerVersion}")
    annotationProcessor("com.google.dagger:dagger-compiler:${Deps.daggerVersion}")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
