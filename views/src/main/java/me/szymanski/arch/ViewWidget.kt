package me.szymanski.arch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface ViewWidget {
    val root: View

    fun inflate(ctx: Context, layoutId: Int, parent: ViewGroup? = null): View =
        LayoutInflater.from(ctx).inflate(layoutId, parent, false)
}
