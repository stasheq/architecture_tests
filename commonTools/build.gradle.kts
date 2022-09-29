plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    ModuleUtils.setupDependencies(this, Modules.commonTools)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
