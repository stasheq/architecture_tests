package me.szymanski.arch.logic.navigation

import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.logic.navigation.StackBehavior.AddIfDifferent
import me.szymanski.arch.logic.navigation.StackBehavior.Clear

sealed interface NavigationScreen {
    val stackBehavior: StackBehavior

    data class List(
        override val stackBehavior: StackBehavior = Clear
    ) : NavigationScreen

    data class Details(
        val repositoryId: RepositoryId,
        override val stackBehavior: StackBehavior = AddIfDifferent
    ) : NavigationScreen

    data class ListAndDetails(
        val repositoryId: RepositoryId?,
        override val stackBehavior: StackBehavior = Clear
    ) : NavigationScreen
}

enum class StackBehavior {
    Add, AddIfDifferent, Clear, Retrieve
}