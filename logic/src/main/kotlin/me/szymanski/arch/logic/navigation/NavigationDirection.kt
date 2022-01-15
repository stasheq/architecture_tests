package me.szymanski.arch.logic.navigation

import me.szymanski.arch.logic.details.RepositoryId

sealed interface NavigationDirection {

    object List : NavigationDirection
    data class Details(val repositoryId: RepositoryId) : NavigationDirection
    data class ListAndDetails(val repositoryId: RepositoryId?) : NavigationDirection
}
