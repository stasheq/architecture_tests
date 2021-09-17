package me.szymanski.arch.logic.test.utils

import io.kotest.assertions.fail
import kotlin.math.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions

fun <T> Flow<T>.test(): TestObserver<T> {
    return TestObserver(CoroutineScope(SupervisorJob() + Dispatchers.Default), this)
}

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {
    val values = mutableListOf<T>()
    val last: T?
        get() = values.lastOrNull()

    private val job: Job = scope.launch {
        flow.collect { values.add(it) }
    }

    fun assertNoValues(): TestObserver<T> {
        Assertions.assertEquals(emptyList<T>(), this.values)
        return this
    }

    fun awaitCount(count: Int, timeout: Long = 1000) = awaitCondition(timeout) { it.size >= count }

    fun awaitValue(value: T, timeout: Long = 1000) {
        val start = min(0, values.size - 1)
        awaitCondition(timeout) {
            it.subList(start, it.size).contains(value)
        }
    }

    fun awaitCondition(timeout: Long = 1000, condition: (values: List<T>) -> Boolean) = runBlocking {
        val start = System.currentTimeMillis()
        while (!condition(values)) {
            delay(100)
            if (System.currentTimeMillis() > start + timeout) {
                fail("awaitCondition timeout")
            }
        }
    }

    fun finish() = job.cancel()
}
