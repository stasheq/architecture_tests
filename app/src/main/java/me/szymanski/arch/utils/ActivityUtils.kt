package me.szymanski.arch.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.szymanski.arch.R

inline fun <reified T : Fragment> AppCompatActivity.changeFragment(frameId: Int = R.id.app_frame, fragment: () -> T) = with(supportFragmentManager) {
    if (findFragmentById(frameId) != null) return@with
    beginTransaction().replace(frameId, fragment()).commit()
}

fun Context.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
fun Fragment.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
