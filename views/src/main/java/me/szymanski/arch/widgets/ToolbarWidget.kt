package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ToolbarBinding

class ToolbarWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    private val binding = inflate(ToolbarBinding::inflate, true).apply {
        toolbarButton.setOnClickListener { backClick() }
    }

    var backClick: () -> Unit = {}
    var title: String = ""
        set(value) {
            field = value
            binding.toolbarTitle.text = title
        }
    var showBackIcon = false
        set(value) {
            field = value
            binding.toolbarButton.isVisible = value
        }
}
