package me.szymanski.arch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.changeFragment(frameId: Int, fragment: Fragment) =
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()

fun Context.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
