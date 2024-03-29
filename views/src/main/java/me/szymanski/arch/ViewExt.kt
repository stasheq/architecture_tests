package me.szymanski.arch

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.SharedFlow

fun <T> Context.inflate(
    inflate: (LayoutInflater, ViewGroup) -> T,
    parent: ViewGroup
): T =
    inflate(LayoutInflater.from(this), parent)

fun <T> Context.inflate(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> T,
    parent: ViewGroup? = null,
    attach: Boolean = false
): T =
    inflate(LayoutInflater.from(this), parent, attach)

fun <T> ViewGroup.inflate(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> T,
    attach: Boolean = false
): T =
    context.inflate(inflate, this, attach)

var SwipeRefreshLayout.refreshing: Boolean
    get() = this.isRefreshing
    set(value) {
        this.isRefreshing = value
    }

fun SwipeRefreshLayout.refreshes(): SharedFlow<Unit> = mutableEventFlow<Unit>().also {
    setOnRefreshListener { it.tryEmit(Unit) }
}

fun TextView.textChanges(): SharedFlow<String> = mutableEventFlow<String>().also {
    val action = Runnable { it.tryEmit(text.toString()) }
    val listener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
        override fun afterTextChanged(s: Editable) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
            debounce(1000, action)
    }
    addTextChangedListener(listener)
}

var TextView.textValue: CharSequence?
    get() = this.text
    set(value) {
        this.text = value
    }

open class ViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

fun View.measure(callback: (width: Int, height: Int) -> Unit) =
    doOnShown { callback(width, height) }

fun View.doOnShown(callback: () -> Unit) = viewTreeObserver.addOnGlobalLayoutListener(
    object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })

fun View.debounce(delay: Long, action: Runnable) {
    removeCallbacks(action)
    postDelayed(action, delay)
}
