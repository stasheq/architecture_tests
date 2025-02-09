package me.szymanski.arch.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.szymanski.arch.details.DetailsScreen
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.domain.navigation.NavigationScreen
import me.szymanski.arch.list.ListScreen
import javax.inject.Inject

@AndroidEntryPoint
class NavigationActivity : ComponentActivity() {

    @Inject
    lateinit var navigationCoordinator: NavigationCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                launch {
                    navigationCoordinator.screenChange.collect {
                        navController.navigate(it)
                    }
                }
                launch {
                    navigationCoordinator.onBackPressed.collect {
                        navController.popBackStack()
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = NavigationScreen.List
            ) {
                composable<NavigationScreen.List> {
                    ListScreen()
                }
                composable<NavigationScreen.Details> {
                    val args = it.toRoute<NavigationScreen.Details>()
                    DetailsScreen(args)
                }
            }
        }
    }
}
