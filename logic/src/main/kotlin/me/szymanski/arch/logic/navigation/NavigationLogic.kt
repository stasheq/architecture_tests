package me.szymanski.arch.logic.navigation

import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.logic.navigation.NavigationDirection.Details
import me.szymanski.arch.logic.navigation.NavigationDirection.List
import me.szymanski.arch.logic.navigation.NavigationDirection.ListAndDetails

interface NavigationLogic {
    fun openDetails(repositoryId: RepositoryId)
    fun onBackPressed()
    var wideScreen: Boolean

    val currentScreen: SharedFlow<NavigationDirection>
    val closeApp: SharedFlow<Unit>
}

class NavigationLogicImpl @Inject constructor() : NavigationLogic {
    override val currentScreen = MutableStateFlow<NavigationDirection>(List)
    override val closeApp = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override var wideScreen: Boolean = false
        set(value) {
            if (value) {
                currentScreen.value = ListAndDetails(null)
            } else {
                val lastValue = currentScreen.value
                currentScreen.value = (lastValue as? ListAndDetails)?.repositoryId?.let { Details(it) } ?: lastValue
            }
            field = value
        }

    override fun openDetails(repositoryId: RepositoryId) {
        currentScreen.value = if (wideScreen) ListAndDetails(repositoryId) else Details(repositoryId)
    }

    override fun onBackPressed() {
        if (currentScreen.value is ListAndDetails || currentScreen.value is List) {
            closeApp.tryEmit(Unit)
        } else {
            currentScreen.value = List
        }
    }
}
