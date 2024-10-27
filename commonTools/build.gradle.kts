plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    ModuleUtils.setupDependencies(this, Modules.commonTools)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
