package me.szymanski.arch.multiplatform

import org.koin.core.context.startKoin
import org.koin.dsl.module

class TextProvider {
    val text = "dodo"
}

class TextPrinter(private val textProvider: TextProvider) {
    fun print(): String = textProvider.text
}

object SomeUtil {

    val myModule = module {
        single { TextPrinter(get()) }
        single { TextProvider() }
    }

    val koin = startKoin {
        modules(myModule)
    }.koin

    fun dodo(): String = koin.get<TextPrinter>().print()
}
