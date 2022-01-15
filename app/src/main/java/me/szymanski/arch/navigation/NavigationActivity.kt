package me.szymanski.arch.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.szymanski.arch.DetailsFragment
import me.szymanski.arch.ListAndDetailsFragment
import me.szymanski.arch.ListFragment
import me.szymanski.arch.R
import me.szymanski.arch.logic.navigation.NavigationDirection
import me.szymanski.arch.logic.navigation.NavigationLogic
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.isWideScreen

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    @Inject
    lateinit var logic: NavigationLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame)
        lifecycleScope.launch {
            whenStarted { subscribeToLogic(logic) }
        }
    }

    private fun CoroutineScope.subscribeToLogic(logic: NavigationLogic) {
        logic.wideScreen = isWideScreen()
        launch { logic.closeApp.collect { finish() } }
        launch { logic.currentScreen.collect { it.updateFragments() } }
    }

    override fun onBackPressed() = logic.onBackPressed()

    private fun NavigationDirection.updateFragments() {
        when (this) {
            is NavigationDirection.Details -> changeFragment { DetailsFragment() }
            NavigationDirection.List -> changeFragment { ListFragment() }
            is NavigationDirection.ListAndDetails -> changeFragment { ListAndDetailsFragment() }
        }
    }
}