package me.szymanski.arch.di

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.szymanski.arch.Logger

@EntryPoint
@Singleton
@InstallIn(SingletonComponent::class)
interface LoggerEntryPoint {
    fun get(): Logger

    companion object {
        fun get(context: Context) = EntryPoints.get(context.applicationContext, LoggerEntryPoint::class.java).get()
    }
}

fun Context.log(msg: String, e: Throwable) = LoggerEntryPoint.get(this).log(msg, e)
fun Context.log(msg: String) = LoggerEntryPoint.get(this).log(msg)
fun Fragment.log(msg: String, e: Throwable) = context?.log(msg, e)
fun Fragment.log(msg: String) = context?.log(msg)
