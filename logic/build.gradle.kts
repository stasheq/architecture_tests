plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutinesVersion}")
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation("io.reactivex.rxjava3:rxjava:3.0.5")
    implementation("com.jakewharton.rxrelay3:rxrelay:3.0.0")
    implementation("com.google.dagger:dagger:${Deps.daggerVersion}")
    annotationProcessor("com.google.dagger:dagger-compiler:${Deps.daggerVersion}")
}
