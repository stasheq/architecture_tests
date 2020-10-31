package me.szymanski.arch

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface ViewWidget {
    val root: View

    fun inflate(ctx: Context, layoutId: Int, parent: ViewGroup? = null): View =
        LayoutInflater.from(ctx).inflate(layoutId, parent, false)
}

fun Activity.setContentView(view: ViewWidget) = setContentView(
    view.root,
    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
)
