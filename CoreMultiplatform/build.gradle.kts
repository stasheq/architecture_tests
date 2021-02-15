plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
}

android {
    compileSdkVersion(Config.targetSDK)
    defaultConfig {
        minSdkVersion(Config.minSDK)
        targetSdkVersion(Config.targetSDK)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
    }
}

kotlin {
    android()
    js().browser()
    iosArm64("ios")
    iosX64("iosx64")

    sourceSets {
        sourceSets["commonMain"].dependencies {
            implementation(Deps.koin)
            implementation(Deps.coroutines)
            implementation(Deps.Ktor.commonCore)
            implementation(Deps.Ktor.commonJson)
            implementation(Deps.Ktor.commonLogging)
            implementation(Deps.Ktor.commonSerialization)
        }

        sourceSets["androidMain"].dependencies {
            implementation(kotlin("stdlib", Deps.Kotlin.version))
            implementation(Deps.coroutines)
            implementation(Deps.Ktor.android)
        }

        sourceSets["iosMain"].dependencies {
            implementation(Deps.Ktor.ios)
        }

        sourceSets["iosx64Main"].dependencies {
            implementation(Deps.Ktor.ios)
        }

        sourceSets["jsMain"].dependencies {
            implementation(Deps.Ktor.js)
        }
    }
}
