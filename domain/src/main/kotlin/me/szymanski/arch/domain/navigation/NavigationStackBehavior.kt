package me.szymanski.arch.domain.navigation

enum class NavigationStackBehavior {
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