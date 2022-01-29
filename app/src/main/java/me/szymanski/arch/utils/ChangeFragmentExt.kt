package me.szymanski.arch.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.szymanski.arch.R

inline fun <reified T : Fragment> AppCompatActivity.changeFragment(fragment: T, frameId: Int = R.id.app_frame) =
    supportFragmentManager.changeFragment(fragment, frameId)

inline fun <reified T : Fragment> Fragment.changeFragment(fragment: T, frameId: Int = R.id.app_frame) =
    childFragmentManager.changeFragment(fragment, frameId)

inline fun <reified T : Fragment> FragmentManager.changeFragment(fragment: T, frameId: Int = R.id.app_frame) {
    val oldFragment = findFragmentById(frameId)
    if (oldFragment != null && oldFragment is T) {
        if (oldFragment.arguments != fragment.arguments)
            oldFragment.arguments = fragment.arguments
        return
    }
    beginTransaction().replace(frameId, fragment).commit()
}
