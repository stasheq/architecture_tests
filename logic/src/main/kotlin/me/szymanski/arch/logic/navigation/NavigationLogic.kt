package me.szymanski.arch.logic.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.logic.navigation.NavigationScreen.Details
import me.szymanski.arch.logic.navigation.NavigationScreen.List
import me.szymanski.arch.logic.navigation.NavigationScreen.ListAndDetails
import me.szymanski.arch.logic.navigation.StackBehavior.Retrieve
import me.szymanski.arch.mutableEventFlow
import javax.inject.Inject

interface NavigationLogic {
    fun openDetails(repositoryId: RepositoryId)
    fun onBackPressed()
    var wideScreen: Boolean

    val currentScreen: SharedFlow<NavigationScreen>
    val closeApp: SharedFlow<Unit>
}

class NavigationLogicImpl @Inject constructor() : NavigationLogic {
    override val currentScreen = MutableStateFlow<NavigationScreen>(List())
    override val closeApp = mutableEventFlow<Unit>()

    override var wideScreen: Boolean = false
        set(value) {
            field = value
            val lastValue = currentScreen.value
            if (value) {
                currentScreen.value = ListAndDetails((lastValue as? Details)?.repositoryId)
            } else {
                currentScreen.value = (lastValue as? ListAndDetails)?.repositoryId?.let { Details(it) } ?: List()
            }
        }

    override fun openDetails(repositoryId: RepositoryId) {
        currentScreen.value = if (wideScreen) ListAndDetails(repositoryId) else Details(repositoryId)
    }

    override fun onBackPressed() {
        if (currentScreen.value is ListAndDetails || currentScreen.value is List) {
            closeApp.tryEmit(Unit)
        } else {
            currentScreen.value = List(Retrieve)
        }
    }
}
