package me.szymanski.ci

import java.io.File
import java.lang.IllegalArgumentException

val gradle = if (System.getProperty("os.name").contains("windows", ignoreCase = true))
    "gradlew.bat" else "./gradlew"
val propertiesFile = File(Const.PROPERTIES_FILE)

fun HashMap<String, String>.toGradleCommand() =
    entries.joinToString(separator = " ") { "-P${it.key}=${it.value}" }

fun HashMap<String, String>.assertNotEmptyValues() =
    entries.forEach { if (it.value.isBlank()) throw IllegalArgumentException("Required value ${it.key} is empty") }

fun readVersionName(): String =
    propertiesFile.readLines().find { it.contains(Const.VERSION_NAME) }!!.substringAfterLast('=')

fun readBuildNumber(): Int =
    propertiesFile.readLines().find { it.contains(Const.BUILD_NUMBER) }!!.substringAfterLast('=').toInt()

fun writeBuildNumber(number: Int) {
    val updatedText = propertiesFile.readLines()
        .joinToString("\n") { if (it.contains(Const.BUILD_NUMBER)) "${Const.BUILD_NUMBER}=$number" else it }
    propertiesFile.writeText(updatedText)
}
