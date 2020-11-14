package me.szymanski.arch.logic.test

import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.Assertions

fun <T> TestObserver<T>.assertLast(value: T) = Assertions.assertEquals(value, values().last())
