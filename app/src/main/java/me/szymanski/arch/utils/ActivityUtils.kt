package me.szymanski.arch.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.szymanski.arch.R

fun AppCompatActivity.changeFragment(frameId: Int, fragment: Fragment) =
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()

fun Context.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
fun Fragment.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
