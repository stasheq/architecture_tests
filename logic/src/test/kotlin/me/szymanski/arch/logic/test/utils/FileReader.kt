package me.szymanski.arch.logic.test.utils

import java.io.BufferedReader

object FileReader {

    fun readText(fileName: String): String =
        BufferedReader(javaClass.getResourceAsStream("/$fileName")!!.reader()).use {
            return@readText it.readText()
        }
}
