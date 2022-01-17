package me.szymanski.arch.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * Eases the Fragment.newInstance ceremony by marking the fragment's args with this delegate
 * Just write the property in newInstance and read it like any other property after the fragment has been created
 *
 * Inspired by Adam Powell, he mentioned it during his IO/17 talk about Kotlin
 *
 * @see https://gist.github.com/yanngx/efdfbf777d21d6f0e73fab4efe47e924
 */
class KotlinFragmentArgsDelegate<T> : kotlin.properties.ReadWriteProperty<Fragment, T> {

    var value: T? = null

    override operator fun getValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>): T {
        if (value == null) {
            val args = thisRef.arguments ?: throw IllegalStateException(
                "Cannot read property ${property.name} if no arguments have been set"
            )
            @Suppress("UNCHECKED_CAST")
            value = args.get(property.name) as T
        }
        return value ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override operator fun setValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>, value: T) {
        val key = property.name
        val args = thisRef.arguments ?: Bundle()
        args.putAll(bundleOf(key to value))
        thisRef.arguments = args
    }
}

fun <T : Any> fragmentArgs() = KotlinFragmentArgsDelegate<T>()
