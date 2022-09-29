object Modules {
    val commonTools = Module(
        name = "commonTools",
        implementationDependencies = listOf(
            Deps.Kotlin.stdlib,
            Deps.Kotlin.coroutines
        ),
        modulesDependencies = emptyList()
    )

    val restApi = Module(
        name = "restApi",
        implementationDependencies = listOf(
            Deps.Retrofit.lib,
            Deps.Retrofit.gson,
            Deps.OkHttp.lib,
            Deps.OkHttp.logging,
            Deps.Dagger.lib
        ),
        modulesDependencies = listOf(
            commonTools
        )
    )

    val domain = Module(
        name = "domain",
        implementationDependencies = listOf(
            Deps.Dagger.lib
        ),
        modulesDependencies = listOf(
            commonTools,
            restApi
        )
    )

    val views = Module(
        name = "views",
        implementationDependencies = listOf(
            Deps.Jetpack.coreKotlin,
            Deps.Jetpack.constraintLayout,
            Deps.Jetpack.swipeRefreshLayout,
            Deps.Jetpack.recyclerView,
            Deps.MaterialComponents.lib
        ),
        modulesDependencies = listOf(
            commonTools
        )
    )

    val app = Module(
        name = "app",
        implementationDependencies = listOf(
            Deps.Jetpack.appCompat,
            Deps.Ktx.activity,
            Deps.Ktx.fragment,
            Deps.Ktx.Lifecycle.runtime,
            Deps.Dagger.lib,
            Deps.Dagger.Hilt.lib
        ),
        modulesDependencies = listOf(
            commonTools,
            restApi,
            domain,
            views
        )
    )
}
