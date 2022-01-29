package me.szymanski.arch.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class KotlinFragmentArgsDelegate<T> : kotlin.properties.ReadWriteProperty<Fragment, T> {

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>): T =
        thisRef.requireArguments().get(property.name) as T

    override operator fun setValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>, value: T) {
        thisRef.arguments = (thisRef.arguments ?: Bundle()).apply {
            putAll(bundleOf(property.name to value))
        }
    }
}

fun <T : Any> fragmentArgs() = KotlinFragmentArgsDelegate<T>()
