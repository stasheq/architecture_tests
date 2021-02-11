plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js().browser()
    iosArm32()
    iosArm64()
    iosX64()
}
