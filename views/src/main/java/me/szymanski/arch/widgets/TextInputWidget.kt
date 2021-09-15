package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputEditText
import me.szymanski.arch.*
import me.szymanski.arch.widgets.databinding.TextInputBinding

class TextInputWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    private val inputText: TextInputEditText

    init {
        inflate(TextInputBinding::inflate, true).apply {
            inputText = inputEditText
        }
    }

    var textValue by inputText::textValue

    val textValueChanges = inputText.textChanges()
}
