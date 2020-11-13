buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
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

task("clean") {
    delete(rootProject.buildDir)
}
