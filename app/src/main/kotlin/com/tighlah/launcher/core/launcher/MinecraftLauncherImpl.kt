package com.tighlah.launcher.core.launcher

import android.content.Context
import com.tighlah.launcher.core.logging.TighlahLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

class MinecraftLauncherImpl(private val context: Context) : MinecraftLauncher {
    private val logger = "LaunchEngine"

    override suspend fun launch(
        configuration: LaunchConfiguration,
        onOutput: (String) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.Default) {
        try {
            val startTime = System.currentTimeMillis()
            TighlahLogger.i(logger, "Launching Minecraft instance: ${configuration.instance.name}")

            val validation = validateConfiguration(configuration)
            if (validation.isFailure) {
                TighlahLogger.e(logger, "Configuration validation failed")
                return@withContext Result.failure(validation.exceptionOrNull() ?: Exception("Validation failed"))
            }

            val javaExe = File(configuration.javaRuntime)
            if (!javaExe.exists()) {
                val error = "Java runtime not found: ${configuration.javaRuntime}"
                TighlahLogger.e(logger, error)
                return@withContext Result.failure(Exception(error))
            }

            val commandBuilder = mutableListOf(javaExe.absolutePath)
            commandBuilder.addAll(configuration.jvmArguments)
            commandBuilder.add("-cp")
            commandBuilder.add(configuration.classpath.joinToString(File.pathSeparator))
            commandBuilder.add(configuration.mainClass)
            commandBuilder.addAll(configuration.gameArguments)

            TighlahLogger.i(logger, "Launch command: ${commandBuilder.take(5).joinToString(" ")}...")

            val process = ProcessBuilder(commandBuilder)
                .directory(File(configuration.instance.gameDirectory))
                .redirectErrorStream(false)
                .apply {
                    environment().putAll(configuration.environmentVariables)
                }
                .start()

            // Read output
            val outputThread = Thread {
                process.inputStream.bufferedReader().use { reader ->
                    reader.forEachLine { line ->
                        onOutput(line)
                        TighlahLogger.d(logger, line)
                    }
                }
            }

            val errorThread = Thread {
                process.errorStream.bufferedReader().use { reader ->
                    reader.forEachLine { line ->
                        onError(line)
                        TighlahLogger.w(logger, line)
                    }
                }
            }

            outputThread.start()
            errorThread.start()

            val completed = process.waitFor(1, TimeUnit.HOURS)
            if (!completed) {
                process.destroy()
                throw TimeoutException("Minecraft launch timeout")
            }

            outputThread.join()
            errorThread.join()

            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            val exitCode = process.exitValue()

            TighlahLogger.i(logger, "Instance finished. Exit code: $exitCode, Duration: ${duration}ms")

            val log = LaunchLog(
                instanceId = configuration.instance.id,
                exitCode = exitCode,
                duration = duration,
                launchedAt = startTime,
                completedAt = endTime
            )

            Result.success(log)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Launch failed", e)
            Result.failure(e)
        }
    }

    override suspend fun validateConfiguration(configuration: LaunchConfiguration) = withContext(Dispatchers.IO) {
        try {
            if (!File(configuration.javaRuntime).exists()) {
                throw Exception("Java runtime not found")
            }
            if (!File(configuration.instance.gameDirectory).exists()) {
                throw Exception("Game directory not found")
            }
            if (configuration.mainClass.isEmpty()) {
                throw Exception("Main class not specified")
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
