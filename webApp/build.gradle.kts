buildDir = File("build")
val core = "CoreMultiplatform"

tasks.register<Copy>("copyJsDistribution") {
    dependsOn(":$core:jsBrowserDistribution")
    from("../$core/build/distributions")
    into(buildDir)
}

tasks.register("assemble") {
    dependsOn("copyJsDistribution")
    doLast {
        println("Done")
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
