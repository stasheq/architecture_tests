package me.szymanski.arch.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.ErrorBarBinding

class ErrorBar(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    private val errorView: TextView

    override val root = ErrorBarBinding.inflate(
        LayoutInflater.from(ctx), parent, false
    ).apply {
        errorView = reposErrorText
    }.root

    var errorText: CharSequence? = errorView.text
        get() = errorView.text
        set(value) {
            errorView.text = value
            root.isVisible = value != null
            field = value
        }
}
