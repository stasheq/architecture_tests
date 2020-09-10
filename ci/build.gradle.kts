plugins {
    id("kotlin")
    id("application")
}

application {
    mainClassName = "me.szymanski.ci.Main"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutines}")
}
