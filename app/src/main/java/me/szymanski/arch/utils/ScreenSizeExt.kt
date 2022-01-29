package me.szymanski.arch.utils

import android.content.Context
import androidx.fragment.app.Fragment
import me.szymanski.arch.R

fun Context.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
fun Fragment.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
