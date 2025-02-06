package me.szymanski.arch.domain.navigation

import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.navigation.NavigationScreen.Details
import me.szymanski.arch.mutableEventFlow
import javax.inject.Inject

interface NavigationCoordinator {
    fun openDetails(repository: Repository)
    fun onBackPressed()

    val screenChange: SharedFlow<NavigationScreen>
    val onBackPressed: SharedFlow<Unit>
}

class NavigationCoordinatorImpl @Inject constructor() : NavigationCoordinator {
    override val screenChange = mutableEventFlow<NavigationScreen>()
    override val onBackPressed = mutableEventFlow<Unit>()

    override fun openDetails(repository: Repository) {
        screenChange.tryEmit(Details(repository.owner, repository.name))
    }

    override fun onBackPressed() {
        onBackPressed.tryEmit(Unit)
    }
}
