package me.szymanski.arch.domain.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.navigation.NavigationScreen.Details
import me.szymanski.arch.domain.navigation.NavigationScreen.List
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.Retrieve
import me.szymanski.arch.mutableEventFlow
import javax.inject.Inject

interface NavigationCoordinator {
    fun openDetails(repository: Repository)
    fun onBackPressed()

    val currentScreen: SharedFlow<NavigationScreen>
    val closeApp: SharedFlow<Unit>
}

class NavigationCoordinatorImpl @Inject constructor() : NavigationCoordinator {
    override val currentScreen = MutableStateFlow<NavigationScreen>(List())
    override val closeApp = mutableEventFlow<Unit>()

    override fun openDetails(repository: Repository) {
        currentScreen.value = Details(repository)
    }

    override fun onBackPressed() {
        when (currentScreen.value) {
            is Details -> currentScreen.value = List(Retrieve)
            is List -> closeApp.tryEmit(Unit)
        }
    }
}
