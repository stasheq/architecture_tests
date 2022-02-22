package me.szymanski.arch.screens

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.R
import me.szymanski.arch.widgets.databinding.ScreenColumnsBinding

class ColumnsScreen(ctx: Context, parent: ViewGroup? = null) : Screen {

    private val binding = ctx.inflate(ScreenColumnsBinding::inflate, parent)
    override val root = binding.root

    companion object {
        val leftColumnId = R.id.column_left
        val rightColumnId = R.id.column_right
    }
}
