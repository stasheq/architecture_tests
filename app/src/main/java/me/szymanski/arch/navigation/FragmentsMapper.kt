package me.szymanski.arch.navigation

import me.szymanski.arch.details.DetailsFragment
import me.szymanski.arch.list.ListFragment
import me.szymanski.arch.domain.navigation.NavigationScreen

fun NavigationScreen.mapToFragment() = when (this) {
    is NavigationScreen.Details -> DetailsFragment.instantiate(repository)
    is NavigationScreen.List -> ListFragment.instantiate()
}
