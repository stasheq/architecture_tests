plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization").version("2.1.10")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging { showStandardStreams = true }
}

dependencies {
    implementation(project(":commonTools"))
    implementation(project(":restApi"))
    implementation(Deps.Dagger.lib)
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Kotlin.serialization)
    kapt(Deps.Dagger.kapt)
    // unit tests
    kaptTest(Deps.Dagger.kapt)
    testImplementation(Deps.OkHttp.mockwebserver)
    testImplementation(Deps.Kotest.lib)
    testImplementation(Deps.Kotest.assertions)
    testImplementation(Deps.Kotest.property)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
