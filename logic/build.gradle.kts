plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":glueKotlin"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutines}")
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofit}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Deps.okhttpLoggingInterceptor}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("com.google.dagger:dagger:${Deps.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Deps.dagger}")
}
