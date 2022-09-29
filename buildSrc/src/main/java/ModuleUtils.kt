import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object ModuleUtils {
    fun setupDependencies(dependencyHandler: DependencyHandler, module: Module, implementation: (dependencyNotation: Any) -> Dependency?) =
        with(dependencyHandler) {
            module.modulesDependencies.forEach { implementation(project(":${it.name}")) }
            module.findDependencies(HashSet(), HashSet()).forEach { implementation(it) }
        }

    private fun Module.findDependencies(found: HashSet<String>, visited: HashSet<Module>): HashSet<String> {
        if (visited.contains(this)) return found
        found.addAll(implementationDependencies)
        visited.add(this)
        modulesDependencies.forEach { submodule ->
            submodule.findDependencies(found, visited)
        }
        return found
    }
}
