package com.tighlah.launcher.core.runtime

import android.content.Context
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.data.model.JavaRuntime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JavaRuntimeManager(private val context: Context) {
    private val logger = "JavaRuntimeManager"

    suspend fun detectJavaRuntimes(): Result<List<JavaRuntime>> = withContext(Dispatchers.IO) {
        try {
            val runtimes = mutableListOf<JavaRuntime>()
            
            // Common Java locations on Android
            val possiblePaths = listOf(
                "/system/app/Java",
                "/data/app/com.termux/java",
                Environment.getExternalFilesDir("java"),
                File(context.filesDir, "java")
            ).filterNotNull()

            possiblePaths.forEach { path ->
                val javaFile = File(path, "bin/java")
                if (javaFile.exists() && javaFile.canExecute()) {
                    val version = detectJavaVersion(javaFile)
                    if (version.isNotEmpty()) {
                        runtimes.add(
                            JavaRuntime(
                                name = "Java $version",
                                path = javaFile.absolutePath,
                                version = version,
                                isValid = true
                            )
                        )
                    }
                }
            }

            TighlahLogger.i(logger, "Detected ${runtimes.size} Java runtimes")
            Result.success(runtimes)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Error detecting Java runtimes", e)
            Result.failure(e)
        }
    }

    private suspend fun detectJavaVersion(javaExe: File): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val process = ProcessBuilder(javaExe.absolutePath, "-version")
                .redirectErrorStream(true)
                .start()
            
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            
            val versionRegex = "(?:java|openjdk) version \"([^\"]+)\"".toRegex()
            versionRegex.find(output)?.groupValues?.get(1) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun validateRuntime(runtime: JavaRuntime): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val javaFile = File(runtime.path)
            if (!javaFile.exists()) {
                return@withContext Result.failure(Exception("Java executable not found"))
            }
            if (!javaFile.canExecute()) {
                return@withContext Result.failure(Exception("Java executable is not executable"))
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

object Environment {
    fun getExternalFilesDir(name: String): File? = null
}
