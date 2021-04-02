buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.kotlin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Deps.hilt}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
