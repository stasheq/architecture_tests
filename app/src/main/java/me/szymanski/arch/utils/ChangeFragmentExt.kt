package me.szymanski.arch.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.szymanski.arch.domain.navigation.NavigationStackBehavior
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.Add
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.AddIfDifferent
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.Clear
import me.szymanski.arch.domain.navigation.NavigationStackBehavior.Retrieve

inline fun <reified T : Fragment> AppCompatActivity.changeFragment(
    fragment: T, stackBehavior: NavigationStackBehavior, frameId: Int
) = supportFragmentManager.changeFragment(fragment, stackBehavior, frameId)

inline fun <reified T : Fragment> Fragment.changeFragment(
    fragment: T, stackBehavior: NavigationStackBehavior, frameId: Int
) = childFragmentManager.changeFragment(fragment, stackBehavior, frameId)

fun FragmentManager.changeFragment(
    fragment: Fragment, stackBehavior: NavigationStackBehavior, frameId: Int
) = when (stackBehavior) {
    Add ->
        add(fragment, frameId)
    AddIfDifferent ->
        addIfDifferent(fragment, findFragmentById(frameId), frameId)
    Clear -> {
        popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        add(fragment, frameId)
    }
    Retrieve -> {
        popBackStackImmediate(fragment.javaClass.name, 0)
        addIfDifferent(fragment, findFragmentById(frameId), frameId)
    }
}

private fun FragmentManager.add(
    fragment: Fragment, frameId: Int
) {
    beginTransaction().replace(frameId, fragment)
        .addToBackStack(fragment.javaClass.name)
        .commit()
}

private fun FragmentManager.addIfDifferent(
    fragment: Fragment, oldFragment: Fragment?, frameId: Int
) {
    if (oldFragment != null && oldFragment.javaClass == fragment.javaClass) {
        if (oldFragment.arguments != fragment.arguments)
            oldFragment.arguments = fragment.arguments
    } else {
        add(fragment, frameId)
    }
}
