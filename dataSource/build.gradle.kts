plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(Deps.Module.commonTools))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Retrofit.lib)
    implementation(Deps.Retrofit.gson)
    implementation(Deps.OkHttp.lib)
    implementation(Deps.OkHttp.logging)
    implementation(Deps.Dagger.lib)
    kapt(Deps.Dagger.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
