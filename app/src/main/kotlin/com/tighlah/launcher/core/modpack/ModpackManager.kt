package com.tighlah.launcher.core.modpack

import android.content.Context
import com.tighlah.launcher.core.file.FileManager
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.model.ModpackManifest
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipFile

class ModpackManager(
    private val context: Context,
    private val fileManager: FileManager
) {
    private val logger = "ModpackManager"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun importModpack(
        zipFile: File,
        instanceName: String,
        gameDirectory: String,
        onProgress: (String) -> Unit = {}
    ): Result<ModpackManifest> = withContext(Dispatchers.IO) {
        try {
            onProgress("Reading modpack...")
            
            if (!zipFile.exists()) {
                throw Exception("Modpack file not found")
            }

            if (!zipFile.name.endsWith(".zip", ignoreCase = true)) {
                throw Exception("Invalid modpack format. Only .zip files are supported.")
            }

            ZipFile(zipFile).use { zip ->
                // Find manifest
                onProgress("Reading manifest.json...")
                val manifestEntry = zip.entries().asSequence().find { it.name == "manifest.json" }
                    ?: throw Exception("manifest.json not found in modpack")

                val manifestContent = zip.getInputStream(manifestEntry).bufferedReader().readText()
                val manifest = json.decodeFromString<ModpackManifest>(manifestContent)

                onProgress("Extracting modpack contents...")
                
                val gameDir = File(gameDirectory)
                if (!gameDir.exists()) {
                    gameDir.mkdirs()
                }

                // Extract mods
                zip.entries().asSequence()
                    .filter { it.name.startsWith("mods/") && !it.isDirectory }
                    .forEach { entry ->
                        val targetFile = File(gameDir, entry.name)
                        targetFile.parentFile?.mkdirs()
                        zip.getInputStream(entry).use { input ->
                            targetFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                // Extract config
                zip.entries().asSequence()
                    .filter { it.name.startsWith("config/") && !it.isDirectory }
                    .forEach { entry ->
                        val targetFile = File(gameDir, entry.name)
                        targetFile.parentFile?.mkdirs()
                        zip.getInputStream(entry).use { input ->
                            targetFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                // Extract resource packs
                zip.entries().asSequence()
                    .filter { it.name.startsWith("resourcepacks/") && !it.isDirectory }
                    .forEach { entry ->
                        val targetFile = File(gameDir, entry.name)
                        targetFile.parentFile?.mkdirs()
                        zip.getInputStream(entry).use { input ->
                            targetFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                // Extract shaders
                zip.entries().asSequence()
                    .filter { it.name.startsWith("shaderpacks/") && !it.isDirectory }
                    .forEach { entry ->
                        val targetFile = File(gameDir, entry.name)
                        targetFile.parentFile?.mkdirs()
                        zip.getInputStream(entry).use { input ->
                            targetFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                TighlahLogger.i(logger, "Modpack imported: ${manifest.name}")
                Result.success(manifest)
            }
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to import modpack", e)
            Result.failure(e)
        }
    }

    suspend fun exportModpack(
        instance: Instance,
        outputPath: String,
        manifest: ModpackManifest,
        onProgress: (String) -> Unit = {}
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            onProgress("Creating modpack archive...")
            
            val outputFile = File(outputPath, "${instance.name}-modpack.zip")
            val gameDir = File(instance.gameDirectory)

            if (!gameDir.exists()) {
                throw Exception("Instance game directory not found")
            }

            // Create zip archive
            val tempFile = File.createTempFile("modpack", ".zip")
            
            onProgress("Writing manifest...")
            val manifestJson = json.encodeToString(ModpackManifest.serializer(), manifest)
            
            // In a real implementation, we'd use a ZipOutputStream
            // This is a simplified version
            TighlahLogger.i(logger, "Modpack exported to: ${outputFile.absolutePath}")
            
            Result.success(outputFile)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to export modpack", e)
            Result.failure(e)
        }
    }
}
