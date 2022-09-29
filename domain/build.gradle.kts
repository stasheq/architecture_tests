plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging { showStandardStreams = true }
}

dependencies {
    ModuleUtils.setupDependencies(this, Modules.domain)
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
