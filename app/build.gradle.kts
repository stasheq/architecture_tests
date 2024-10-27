plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Config.targetSDK
    buildToolsVersion = Config.buildTools
    namespace = Config.appId

    defaultConfig {
        applicationId = Config.appId
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
            versionSuffix: String? = null,
            config: VariantConfig
        ) = maybeCreate(name).apply {
            isMinifyEnabled = minify
            if (proguard) proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", appNameRes)
            if (idSuffix != null) applicationIdSuffix = ".$idSuffix"
            if (versionSuffix != null) versionNameSuffix = " $versionSuffix"
            buildConfigField("String", "CONF_ENVIRONMENT", "\"${config.environment}\"")
            buildConfigField("String", "CONF_USER", "\"${config.defaultUser}\"")
            buildConfigField("int", "CONF_PAGE_LIMIT", "${config.listPageLimit}")
            buildConfigField("int", "CONF_REST_TIMEOUT", "${config.restCallTimeout}")
            buildConfigField("boolean", "CONF_API_LOG", "${config.apiLogEnabled}")
        }

        buildType(
            name = "debug",
            minify = false,
            proguard = false,
            appNameRes = "@string/app_name_debug",
            idSuffix = "debug",
            versionSuffix = "debug",
            config = DebugVariantConfig
        )

        buildType(
            name = "release",
            appNameRes = "@string/app_name_release",
            config = ReleaseVariantConfig
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
        buildConfig = true
    }
}

dependencies {
    debugImplementation(Deps.LeakCanary.lib)
    ModuleUtils.setupDependencies(this, Modules.app)
    kapt(Deps.Dagger.Hilt.kapt)
}

abstract class VariantConfig {
    abstract val environment: String
    abstract val apiLogEnabled: Boolean
    open val defaultUser = "google"
    open val listPageLimit = 20
    open val restCallTimeout = 5000
}

object DebugVariantConfig : VariantConfig() {
    override val environment = "https://api.github.com/"
    override val apiLogEnabled: Boolean = true
}

object ReleaseVariantConfig : VariantConfig() {
    override val environment = "https://api.github.com/"
    override val apiLogEnabled: Boolean = false
}
