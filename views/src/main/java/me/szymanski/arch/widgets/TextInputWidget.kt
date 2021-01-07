package me.szymanski.arch.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.Observable
import me.szymanski.arch.*
import me.szymanski.arch.widgets.databinding.TextInputBinding
import java.util.concurrent.TimeUnit

class TextInputWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val inputText: TextInputEditText

    override val root: View = ctx.inflate(TextInputBinding::inflate, parent).apply {
        inputText = inputEditText
    }.root

    var textValue by inputText::textValue
    val textValueChanges: Observable<String> =
        inputText.textChanges().debounce(1000, TimeUnit.MILLISECONDS).map { it.toString() }
}
