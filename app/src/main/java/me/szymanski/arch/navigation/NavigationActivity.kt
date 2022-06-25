package me.szymanski.arch.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.szymanski.arch.R
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.isWideScreen

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationCoordinator: NavigationCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_frame)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            whenStarted { subscribeToNavigationCoordinator(navigationCoordinator) }
        }
    }

    private fun CoroutineScope.subscribeToNavigationCoordinator(logic: NavigationCoordinator) {
        logic.wideScreen = isWideScreen()
        launch {
            logic.closeApp.collect { finish() }
        }
        launch {
            logic.currentScreen.collect {
                changeFragment(it.mapToFragment(), it.stackBehavior, R.id.app_frame)
            }
        }
    }

    override fun onBackPressed() = navigationCoordinator.onBackPressed()
}
