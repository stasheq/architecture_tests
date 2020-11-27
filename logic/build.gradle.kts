plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    test {
        java {
            srcDir("build/generated/source/kapt/test")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutines}")
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofit}")
    implementation("com.squareup.okhttp3:okhttp:${Deps.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Deps.okhttp}")
    implementation("io.reactivex.rxjava3:rxjava:${Deps.rxJava}")
    implementation("com.jakewharton.rxrelay3:rxrelay:${Deps.rxRelay}")
    implementation("com.google.dagger:dagger:${Deps.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Deps.dagger}")
    // unit tests
    kaptTest("com.google.dagger:dagger-compiler:${Deps.dagger}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Deps.okhttp}")
    testImplementation("io.kotest:kotest-runner-junit5:${Deps.kotest}")
    testImplementation("io.kotest:kotest-assertions-core:${Deps.kotest}")
    testImplementation("io.kotest:kotest-property:${Deps.kotest}")
}
