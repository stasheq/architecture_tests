package me.szymanski.arch.navigation

import me.szymanski.arch.DetailsFragment
import me.szymanski.arch.ListAndDetailsFragment
import me.szymanski.arch.ListFragment
import me.szymanski.arch.domain.navigation.NavigationScreen

fun NavigationScreen.mapToFragment() = when (this) {
    is NavigationScreen.Details -> DetailsFragment.instantiate(repositoryId)
    is NavigationScreen.List -> ListFragment.instantiate()
    is NavigationScreen.ListAndDetails -> ListAndDetailsFragment.instantiate(repositoryId)
}
