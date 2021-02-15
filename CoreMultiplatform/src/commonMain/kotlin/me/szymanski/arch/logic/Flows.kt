package me.szymanski.arch.logic

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> replayFlow() =
    MutableSharedFlow<T>(replay = 1, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

fun <T> publishFlow() =
    MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

val <T> MutableSharedFlow<T>.lastValue: T?
    get() = replayCache.lastOrNull()

fun <T> MutableSharedFlow<T>.publish(value: T) = tryEmit(value)
