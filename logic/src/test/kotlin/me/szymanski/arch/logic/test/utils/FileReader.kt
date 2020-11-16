package me.szymanski.arch.logic.test.utils

import java.io.BufferedReader

object FileReader {
    fun readText(fileName: String): String {
        val reader = BufferedReader(javaClass.getResourceAsStream("/$fileName").reader())
        val content: String
        reader.use { content = it.readText() }
        return content
    }
}
