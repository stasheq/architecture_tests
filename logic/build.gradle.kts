plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutinesVersion}")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}
