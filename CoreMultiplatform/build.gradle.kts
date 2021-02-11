plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    js().browser()
    iosArm32()
    iosArm64()
    iosX64()
    android()
}
