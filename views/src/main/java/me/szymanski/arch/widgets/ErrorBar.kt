package me.szymanski.arch.widgets

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.error_bar.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.ViewWidget.Companion.inflate

class ErrorBar(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    private val errorView: TextView
    override val root = inflate(ctx, R.layout.error_bar, parent).apply {
        errorView = reposErrorText
    }

    var errorText: CharSequence? = errorView.text
        get() = errorView.text
        set(value) {
            errorView.text = value
            root.isVisible = value != null
            field = value
        }
}
