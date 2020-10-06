package me.szymanski.arch.widgets

import android.content.Context
import android.view.ViewGroup
import com.example.widgets.R
import kotlinx.android.synthetic.main.frame_single.view.*
import me.szymanski.arch.ViewWidget

class FrameSingle(ctx: Context) : ViewWidget {
    override val root = inflate(ctx, R.layout.frame_single)
    val frame: ViewGroup = root.singleFrame
}
