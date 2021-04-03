package me.szymanski.arch.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.Observable
import me.szymanski.arch.*
import me.szymanski.arch.widgets.databinding.TextInputBinding
import java.util.concurrent.TimeUnit

class TextInputWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    private val inputText: TextInputEditText

    init {
        inflate(TextInputBinding::inflate, true).apply {
            inputText = inputEditText
        }
    }

    var textValue by inputText::textValue
    val textValueChanges: Observable<String> =
        inputText.textChanges().debounce(1000, TimeUnit.MILLISECONDS).map { it.toString() }
}
