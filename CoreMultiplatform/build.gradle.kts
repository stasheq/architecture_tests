plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js().browser()
    iosArm32()
    iosArm64()
    iosX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.koin:koin-core:${Deps.koin}")
            }
        }
    }
}
