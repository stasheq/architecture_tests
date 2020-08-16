buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.kotlinVersion}")
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
