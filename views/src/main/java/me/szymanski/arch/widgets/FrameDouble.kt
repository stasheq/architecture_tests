package me.szymanski.arch.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.FrameDoubleBinding

class FrameDouble(ctx: Context) : ViewWidget {
    val rightColumn: ViewGroup
    val leftColumn: ViewGroup

    override val root = FrameDoubleBinding.inflate(LayoutInflater.from(ctx)).apply {
        rightColumn = columnRight
        leftColumn = columnLeft
    }.root
}
