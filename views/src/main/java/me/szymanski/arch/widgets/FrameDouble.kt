package me.szymanski.arch.widgets

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.widgets.R
import kotlinx.android.synthetic.main.frame_double.view.*
import me.szymanski.glue.ViewWidget

class FrameDouble(ctx: Context) : ViewWidget {

    override val root = inflate(ctx, R.layout.frame_double)
    val rightColumn: ViewGroup = root.columnRight
    val leftColumn: ViewGroup = root.columnLeft
    var isLeftPanelVisible = false
        set(value) {
            field = value
            leftColumn.isVisible = value
        }
}