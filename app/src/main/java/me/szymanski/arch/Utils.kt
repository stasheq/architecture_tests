package me.szymanski.arch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.changeFragment(frameId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    val transaction = supportFragmentManager.beginTransaction().replace(frameId, fragment)
    if (addToBackStack) transaction.addToBackStack(null)
    transaction.commit()
}

fun Context.isWideScreen() = resources.getBoolean(R.bool.isWideScreen)
