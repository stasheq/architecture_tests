package me.szymanski.arch.logic.navigation

import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.logic.navigation.StackBehavior.AddIfDifferent
import me.szymanski.arch.logic.navigation.StackBehavior.Retrieve

sealed interface NavigationScreen {
    val stackBehavior: StackBehavior

    data class List(
        override val stackBehavior: StackBehavior = Retrieve
    ) : NavigationScreen

    data class Details(
        val repositoryId: RepositoryId,
        override val stackBehavior: StackBehavior = AddIfDifferent
    ) : NavigationScreen

    data class ListAndDetails(
        val repositoryId: RepositoryId?,
        override val stackBehavior: StackBehavior = Retrieve
    ) : NavigationScreen
}

enum class StackBehavior {
    /**
     * Adds new screen to stack.
     */
    Add,

    /**
     * Adds new screen to stack, when current screen is of a different class.
     * Updates args if current fragment is of the same class.
     */
    AddIfDifferent,

    /**
     * Clears stack to an empty state and then adds screen as first element.
     */
    Clear,

    /**
     * Searches for this screen class in a stack. If found, clears top of the stack
     * and updates args in the found fragment. If not found, works same as Clear.
     */
    Retrieve
}