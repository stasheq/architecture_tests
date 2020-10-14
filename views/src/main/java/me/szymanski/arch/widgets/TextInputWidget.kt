package me.szymanski.arch.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import me.szymanski.glue.ViewWidget
import kotlinx.android.synthetic.main.text_input.view.*
import me.szymanski.arch.textValue
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import java.util.concurrent.TimeUnit

class TextInputWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    override val root: View = inflate(ctx, R.layout.text_input, parent)

    var textValue by root.inputEditText::textValue
    val textValueChanges: Observable<CharSequence> =
        root.inputEditText.textChanges().debounce(50, TimeUnit.MILLISECONDS)
}
