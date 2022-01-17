package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ErrorBarBinding

class ErrorBarWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {
    private val binding = inflate(ErrorBarBinding::inflate, true)

    init {
        isVisible = false
    }

    var errorText: CharSequence? = binding.reposErrorText.text
        get() = binding.reposErrorText.text
        set(value) {
            binding.reposErrorText.text = value
            isVisible = value != null
            field = value
        }
}
