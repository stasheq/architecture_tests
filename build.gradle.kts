plugins {
    id("com.android.application").version("8.8.0").apply(false)
    id("com.google.dagger.hilt.android").version(Deps.Dagger.version).apply(false)
    id("org.jetbrains.kotlin.android").version(Deps.Kotlin.version).apply(false)
    id("org.jetbrains.kotlin.plugin.compose").version(Deps.Kotlin.version).apply(false)
}
