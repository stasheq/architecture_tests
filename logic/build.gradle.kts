plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging { showStandardStreams = true }
}

sourceSets {
    test {
        java {
            srcDir("build/generated/source/kapt/test")
        }
    }
}

dependencies {
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Retrofit.lib)
    implementation(Deps.Retrofit.gson)
    implementation(Deps.OkHttp.lib)
    implementation(Deps.OkHttp.logging)
    implementation(Deps.Dagger.lib)
    kapt(Deps.Dagger.kapt)
    // unit tests
    kaptTest(Deps.Dagger.kapt)
    testImplementation(Deps.OkHttp.mockwebserver)
    testImplementation(Deps.Kotest.lib)
    testImplementation(Deps.Kotest.assertions)
    testImplementation(Deps.Kotest.property)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
