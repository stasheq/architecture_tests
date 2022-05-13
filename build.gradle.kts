buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.Kotlin.version}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Deps.Dagger.version}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
