plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":glueKotlin"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutinesVersion}")
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJavaVersion}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelayVersion}")
    implementation("com.google.dagger:dagger:${Deps.daggerVersion}")
    kapt("com.google.dagger:dagger-compiler:${Deps.daggerVersion}")
}
