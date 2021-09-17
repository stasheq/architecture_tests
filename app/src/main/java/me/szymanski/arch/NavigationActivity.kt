package me.szymanski.arch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.screenslogic.NavigationLogic
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.widgets.FrameDouble.State.BOTH
import me.szymanski.arch.widgets.FrameDouble.State.LEFT
import me.szymanski.arch.widgets.FrameDouble.State.RIGHT

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    @Inject
    lateinit var logic: NavigationLogic
    lateinit var view: FrameDouble

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = FrameDouble(this)
        setContentView(view)
        if (savedInstanceState == null) view.apply {
            changeFragment(leftColumn.id, ListFragment())
            changeFragment(rightColumn.id, DetailsFragment())
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToLogic(view, logic)
            }
        }
    }

    private suspend fun subscribeToLogic(view: FrameDouble, logic: NavigationLogic) = coroutineScope {
        var initiated = false
        logic.wideScreen = isWideScreen()
        launch { logic.closeApp.collect { finish() } }
        launch {
            logic.currentScreen.map {
                when (it) {
                    NavigationLogic.Screen.LIST -> LEFT
                    NavigationLogic.Screen.DETAILS -> RIGHT
                    NavigationLogic.Screen.LIST_AND_DETAILS -> BOTH
                }
            }.collect {
                view.setState(it, initiated)
                initiated = true
            }
        }
    }

    override fun onBackPressed() = logic.onBackPressed()
}
