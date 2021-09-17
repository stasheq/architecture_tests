package me.szymanski.arch.logic.screenslogic

import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.logic.screenslogic.NavigationLogic.Screen.DETAILS
import me.szymanski.arch.logic.screenslogic.NavigationLogic.Screen.LIST
import me.szymanski.arch.logic.screenslogic.NavigationLogic.Screen.LIST_AND_DETAILS

interface NavigationLogic {
    fun openDetails()
    fun onBackPressed()
    var wideScreen: Boolean

    val currentScreen: SharedFlow<Screen>
    val closeApp: SharedFlow<Unit>

    enum class Screen { LIST, DETAILS, LIST_AND_DETAILS }
}

class NavigationLogicImpl @Inject constructor() : NavigationLogic {
    override val currentScreen = MutableStateFlow(LIST)
    override val closeApp = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var detailsOpen = false

    override var wideScreen: Boolean = false
        set(value) {
            if (value) {
                currentScreen.value = LIST_AND_DETAILS
            } else {
                val lastValue = currentScreen.value
                currentScreen.value = if (lastValue == LIST || !detailsOpen) LIST else DETAILS
            }
            field = value
        }

    override fun openDetails() {
        detailsOpen = true
        currentScreen.value = if (wideScreen) LIST_AND_DETAILS else DETAILS
    }

    override fun onBackPressed() {
        detailsOpen = false
        if (currentScreen.value == LIST_AND_DETAILS || currentScreen.value == LIST) {
            closeApp.tryEmit(Unit)
        } else {
            currentScreen.value = LIST
        }
    }
}
