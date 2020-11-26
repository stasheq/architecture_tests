package me.szymanski.arch

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable

var SwipeRefreshLayout.refreshing: Boolean
    get() = this.isRefreshing
    set(value) {
        this.isRefreshing = value
    }

fun SwipeRefreshLayout.refreshes(): Observable<Unit> {
    val relay = PublishRelay.create<Unit>()
    setOnRefreshListener { relay.accept(Unit) }
    relay.doOnDispose { setOnRefreshListener(null) }
    return relay
}

var TextView.textValue: CharSequence?
    get() = this.text
    set(value) {
        this.text = value
    }

open class ViewHolder<T: ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

operator fun <R, T> KProperty0<T>.getValue(thisRef: R, property: KProperty<*>) = get()

operator fun <R, T> KMutableProperty0<T>.setValue(thisRef: R, property: KProperty<*>, value: T) = set(value)
