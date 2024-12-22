package me.szymanski.arch.domain.navigation

import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.AddIfDifferent
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.Retrieve

sealed interface NavigationScreen {
    val stackBehavior: NavigationStackBehavior

    data class List(
        override val stackBehavior: NavigationStackBehavior = Retrieve
    ) : NavigationScreen

    data class Details(
        val repositoryId: RepositoryId,
        override val stackBehavior: NavigationStackBehavior = AddIfDifferent
    ) : NavigationScreen
}
