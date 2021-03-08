package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ErrorBarBinding

class ErrorBar @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {
    private val errorView: TextView

    init {
        ctx.inflate(ErrorBarBinding::inflate, this).apply {
            errorView = reposErrorText
        }.root.apply { isVisible = false }
    }

    var errorText: CharSequence? = errorView.text
        get() = errorView.text
        set(value) {
            errorView.text = value
            this.isVisible = value != null
            field = value
        }
}
