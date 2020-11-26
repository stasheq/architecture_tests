package me.szymanski.arch

import android.app.Activity
import android.view.View
import android.view.ViewGroup

interface ViewWidget {
    val root: View
}

fun Activity.setContentView(view: ViewWidget) = setContentView(
    view.root,
    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
)
