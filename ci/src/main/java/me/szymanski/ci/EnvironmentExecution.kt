package me.szymanski.ci

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

fun String.exec(
    workingDir: File? = null,
    throwOnError: Boolean = true,
    hideCommand: Boolean = false
): ExecOutput = toParts().exec(workingDir, throwOnError, hideCommand)

fun List<String>.exec(
    workingDir: File? = null,
    throwOnError: Boolean = true,
    hideCommand: Boolean = false
): ExecOutput {
    println(this.printCommand(hideCommand))
    val proc = ProcessBuilder(this)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val outputBuilder = StringBuilder()
    val outputJob = scope.launch { readStream(proc.inputStream, outputBuilder, System.out) }
    val errorBuilder = StringBuilder()
    val errorJob = scope.launch { readStream(proc.errorStream, errorBuilder, System.out) }

    proc.waitFor(Const.MAX_COMMAND_MINUTES, TimeUnit.MINUTES)
    runBlocking {
        withTimeout(Const.MAX_READING_STREAMS_AFTER_COMMAND_END_MS) {
            outputJob.join()
            errorJob.join()
        }
    }

    val exitCode = proc.exitValue()
    val result = ExecOutput(this, outputBuilder.toString().trim(), errorBuilder.toString().trim(), exitCode)

    if (!result.isSuccess() && throwOnError) {
        throw CommandFailedException(result, hideCommand)
    }
    return result
}

fun String.toParts(): List<String> = this.split(whiteCharsRegex)

fun List<String>.printCommand(hideCommand: Boolean = false): String = when {
    !hideCommand -> joinToString(" ") { if (it.contains(whiteCharsRegex)) "\"$it\"" else it }
    size > 2 -> "${this[0]} ${this[1]}..."
    size > 1 -> "${this[0]}..."
    else -> "..."
}

data class ExecOutput(val command: List<String>, val output: String, val error: String, val exitCode: Int) {
    fun isSuccess(): Boolean = exitCode == 0
}

class CommandFailedException(result: ExecOutput, hideCommand: Boolean) :
    RuntimeException("Command '${result.command.printCommand(hideCommand)}' failed with exit code ${result.exitCode}")

suspend fun readStream(
    inputStream: InputStream,
    stringBuilder: StringBuilder,
    printer: PrintStream? = null
) = coroutineScope {
    val reader = inputStream.bufferedReader()
    while (isActive) {
        val line: String = reader.readLine() ?: break
        printer?.println(line)
        stringBuilder.append(line)
        stringBuilder.append('\n')
    }
    reader.close()
}

val whiteCharsRegex = "\\s".toRegex()
