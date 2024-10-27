plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":commonTools"))
    implementation(Deps.Retrofit.lib)
    implementation(Deps.Retrofit.gson)
    implementation(Deps.OkHttp.lib)
    implementation(Deps.OkHttp.logging)
    implementation(Deps.Dagger.lib)
    kapt(Deps.Dagger.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
