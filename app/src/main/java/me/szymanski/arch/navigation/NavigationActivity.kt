package me.szymanski.arch.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.szymanski.arch.R
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.utils.changeFragment
import javax.inject.Inject

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationCoordinator: NavigationCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_frame)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToNavigationCoordinator(navigationCoordinator)
            }
        }
    }

    private fun CoroutineScope.subscribeToNavigationCoordinator(logic: NavigationCoordinator) {
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
