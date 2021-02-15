buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Deps.hilt}")
        classpath(kotlin("serialization", Deps.Kotlin.version))
        classpath(kotlin("gradle-plugin", Deps.Kotlin.version))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
