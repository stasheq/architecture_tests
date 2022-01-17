package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.setValue
import me.szymanski.arch.textChanges
import me.szymanski.arch.textValue
import me.szymanski.arch.widgets.databinding.TextInputBinding

class TextInputWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    private val binding = inflate(TextInputBinding::inflate, true)
    var textValue by binding.inputEditText::textValue
    val textValueChanges = binding.inputEditText.textChanges()
}
