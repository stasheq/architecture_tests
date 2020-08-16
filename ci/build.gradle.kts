plugins {
    id("kotlin")
    id("application")
}

application {
    mainClassName = "me.szymanski.ci.Main"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutinesVersion}")
}
