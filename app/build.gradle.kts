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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "me.szymanski.arch"
}

dependencies {
    debugImplementation(Deps.LeakCanary.lib)
    implementation(project(Deps.Module.commonTools))
    implementation(project(Deps.Module.restApi))
    implementation(project(Deps.Module.logic))
    implementation(project(Deps.Module.views))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Jetpack.coreKotlin)
    implementation(Deps.Jetpack.appCompat)
    implementation(Deps.Jetpack.constraintLayout)
    implementation(Deps.Jetpack.swipeRefreshLayout)
    implementation(Deps.Jetpack.recyclerView)
    implementation(Deps.MaterialComponents.lib)
    implementation(Deps.Ktx.activity)
    implementation(Deps.Ktx.fragment)
    implementation(Deps.Ktx.Lifecycle.runtime)
    implementation(Deps.OkHttp.lib)
    implementation(Deps.Dagger.lib)
    implementation(Deps.Dagger.Hilt.lib)
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
