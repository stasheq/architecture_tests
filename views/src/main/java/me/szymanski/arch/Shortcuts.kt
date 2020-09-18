package me.szymanski.arch

import android.widget.TextView
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

var SwipeRefreshLayout.refreshing: Boolean
    get() = this.isRefreshing
    set(value) {
        this.isRefreshing = value
    }

var TextView.textValue: CharSequence?
    get() = this.text
    set(value) {
        this.text = value
    }

operator fun <R, T> KProperty0<T>.getValue(thisRef: R, property: KProperty<*>) = get()

operator fun <R, T> KMutableProperty0<T>.setValue(thisRef: R, property: KProperty<*>, value: T) = set(value)
