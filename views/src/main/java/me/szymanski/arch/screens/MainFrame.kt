package me.szymanski.arch.screens

import android.content.Context
import com.example.widgets.R
import me.szymanski.arch.ViewWidget

class MainFrame(ctx: Context) : ViewWidget {
    val frameId = R.id.mainFrame
    override val root = inflate(ctx, R.layout.main_frame)
}
