package me.szymanski.arch.navigation

import me.szymanski.arch.details.DetailsFragment
import me.szymanski.arch.ListFragment
import me.szymanski.arch.domain.navigation.NavigationScreen

fun NavigationScreen.mapToFragment() = when (this) {
    is NavigationScreen.Details -> DetailsFragment.instantiate(repositoryId)
    is NavigationScreen.List -> ListFragment.instantiate()
}
