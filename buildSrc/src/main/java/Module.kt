data class Module(
    val name: String,
    val implementationDependencies: List<String> = emptyList(),
    val modulesDependencies: List<Module> = emptyList()
)
