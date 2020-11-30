package me.szymanski.arch.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.ToolbarBinding

class Toolbar(val ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    val button: ImageView
    val titleView: TextView

    override val root = ToolbarBinding.inflate(
        LayoutInflater.from(ctx), parent, false
    ).apply {
        button = toolbarButton
        button.setOnClickListener { backClick() }
        titleView = toolbarTitle
    }.root

    var backClick: () -> Unit = {}
    var title: String = ""
        set(value) {
            field = value
            titleView.text = title
        }
    var showBackIcon = false
        set(value) {
            field = value
            button.isVisible = value
        }
}
