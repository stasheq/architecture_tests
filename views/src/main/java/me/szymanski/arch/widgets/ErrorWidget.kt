package me.szymanski.arch.widgets

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.error_center.view.*
import me.szymanski.arch.ViewWidget

class ErrorWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    private val errorView: TextView
    override val root = inflate(ctx, R.layout.error_center, parent).apply {
        errorView = reposErrorText
    }

    var errorText: CharSequence? = errorView.text
        get() = errorView.text
        set(value) {
            errorView.text = value
            root.isVisible = field != null
            field = value
        }
}
