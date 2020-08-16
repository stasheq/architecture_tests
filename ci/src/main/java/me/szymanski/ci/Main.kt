package me.szymanski.ci

import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            if (args.isEmpty()) throw IllegalArgumentException("No command")
            val command = args.first()
            println("Command: $command")
            "git status".exec()
            when (command) {
                // TODO implement commands
                else -> throw IllegalArgumentException("Wrong command")
            }
        } catch (e: Exception) {
            if (e !is CommandFailedException && e !is IllegalArgumentException) e.printStackTrace()
            println(e.message)
            exitProcess(Const.SCRIPT_FAILED_CODE)
        }
    }
}

object Const {
    const val SCRIPT_FAILED_CODE = 42

    // gradle.properties
    const val PROPERTIES_FILE = "gradle.properties"
    const val VERSION_NAME = "VERSION_NAME"
    const val BUILD_NUMBER = "BUILD_NUMBER"
    const val SIGN_STORE_FILE = "SIGN_STORE_FILE"
    const val SIGN_KEY_ALIAS = "SIGN_KEY_ALIAS"
    const val SIGN_STORE_PASSWORD = "SIGN_STORE_PASSWORD"
    const val SIGN_KEY_PASSWORD = "SIGN_KEY_PASSWORD"
}


