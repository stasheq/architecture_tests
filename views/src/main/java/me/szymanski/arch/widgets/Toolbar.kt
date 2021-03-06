package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ToolbarBinding

class Toolbar @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : LinearLayout(ctx, attrs) {
    private val button: ImageView
    private val titleView: TextView

    init {
        ctx.inflate(ToolbarBinding::inflate, this).apply {
            button = toolbarButton
            button.setOnClickListener { backClick() }
            titleView = toolbarTitle
        }
    }

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
