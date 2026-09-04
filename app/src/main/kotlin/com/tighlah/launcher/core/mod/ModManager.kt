package com.tighlah.launcher.core.mod

import android.content.Context
import com.tighlah.launcher.core.file.FileManager
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.model.Mod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.jar.JarFile

class ModManager(
    private val context: Context,
    private val fileManager: FileManager
) {
    private val logger = "ModManager"

    suspend fun importMod(
        instance: Instance,
        sourceFile: File,
        onProgress: (String) -> Unit = {}
    ): Result<Mod> = withContext(Dispatchers.IO) {
        try {
            onProgress("Validating mod file...")
            
            if (!sourceFile.exists()) {
                throw Exception("Mod file not found: ${sourceFile.absolutePath}")
            }

            if (!sourceFile.name.endsWith(".jar", ignoreCase = true)) {
                throw Exception("Invalid mod format. Only .jar files are supported.")
            }

            onProgress("Reading mod metadata...")
            val modMetadata = extractModMetadata(sourceFile)
            
            val modsDir = instance.getModsDirectory()
            if (!modsDir.exists()) {
                modsDir.mkdirs()
            }

            val destinationFile = File(modsDir, sourceFile.name)
            if (destinationFile.exists()) {
                throw Exception("Mod with same filename already exists")
            }

            onProgress("Copying mod to instance...")
            fileManager.copyFile(sourceFile, destinationFile).getOrThrow()

            val fileSize = destinationFile.length()
            TighlahLogger.i(logger, "Mod imported: ${sourceFile.name} (${fileSize / 1024}KB)")

            val mod = Mod(
                instanceId = instance.id,
                name = modMetadata["name"] ?: sourceFile.nameWithoutExtension,
                filename = sourceFile.name,
                filePath = destinationFile.absolutePath,
                version = modMetadata["version"] ?: "",
                fileSize = fileSize,
                isEnabled = true,
                description = modMetadata["description"] ?: "",
                author = modMetadata["author"] ?: ""
            )

            Result.success(mod)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to import mod", e)
            Result.failure(e)
        }
    }

    suspend fun deleteMod(instance: Instance, mod: Mod): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val modFile = File(mod.filePath)
            if (modFile.exists()) {
                fileManager.deleteFile(modFile).getOrThrow()
                TighlahLogger.i(logger, "Mod deleted: ${mod.filename}")
            }
            Result.success(true)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to delete mod", e)
            Result.failure(e)
        }
    }

    suspend fun toggleMod(instance: Instance, mod: Mod, enable: Boolean): Result<File> = withContext(Dispatchers.IO) {
        try {
            val modFile = File(mod.filePath)
            if (!modFile.exists()) {
                throw Exception("Mod file not found")
            }

            val newFile = if (enable) {
                File(modFile.parent, mod.filename)
            } else {
                File(modFile.parent, mod.getDisabledFilename())
            }

            fileManager.renameFile(modFile, newFile.name).getOrThrow()
            TighlahLogger.i(logger, "Mod toggled: ${mod.filename} -> ${if (enable) "ENABLED" else "DISABLED"}")
            
            Result.success(newFile)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to toggle mod", e)
            Result.failure(e)
        }
    }

    private suspend fun extractModMetadata(jarFile: File): Map<String, String> = withContext(Dispatchers.IO) {
        val metadata = mutableMapOf<String, String>()
        
        try {
            JarFile(jarFile).use { jar ->
                // Try to read from fabric.mod.json
                val fabricEntry = jar.getEntry("fabric.mod.json")
                if (fabricEntry != null) {
                    val content = jar.getInputStream(fabricEntry).bufferedReader().readText()
                    metadata["name"] = extractJsonValue(content, "name")
                    metadata["version"] = extractJsonValue(content, "version")
                    metadata["description"] = extractJsonValue(content, "description")
                    metadata["author"] = extractJsonValue(content, "author")
                }
                
                // Try to read from mcmod.info (Forge)
                val forgeEntry = jar.getEntry("mcmod.info")
                if (forgeEntry != null && metadata.isEmpty()) {
                    val content = jar.getInputStream(forgeEntry).bufferedReader().readText()
                    metadata["name"] = extractJsonValue(content, "name")
                    metadata["version"] = extractJsonValue(content, "version")
                    metadata["description"] = extractJsonValue(content, "description")
                    metadata["authorList"] = extractJsonValue(content, "authorList")
                }
            }
        } catch (e: Exception) {
            TighlahLogger.w(logger, "Could not extract metadata from jar", e)
        }
        
        return@withContext metadata
    }

    private fun extractJsonValue(json: String, key: String): String {
        val regex = "\"$key\"\s*:\s*\"([^\"]*)\"".toRegex()
        return regex.find(json)?.groupValues?.get(1) ?: ""
    }
}
