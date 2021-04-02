buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.Kotlin.version}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Deps.Dagger.Hilt.version}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
