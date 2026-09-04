package com.tighlah.launcher.core.file

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FileManager(private val context: Context) {
    
    suspend fun createDirectory(path: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            Result.success(dir)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun copyFile(source: File, destination: File): Result<File> = withContext(Dispatchers.IO) {
        try {
            source.inputStream().use { input ->
                destination.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Result.success(destination)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun moveFile(source: File, destination: File): Result<File> = withContext(Dispatchers.IO) {
        try {
            copyFile(source, destination).getOrThrow()
            if (!source.delete()) {
                throw IOException("Failed to delete source file")
            }
            Result.success(destination)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFile(file: File): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val deleted = file.delete()
            Result.success(deleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun renameFile(file: File, newName: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val newFile = File(file.parent, newName)
            if (!file.renameTo(newFile)) {
                throw IOException("Failed to rename file")
            }
            Result.success(newFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listFiles(path: String): Result<List<File>> = withContext(Dispatchers.IO) {
        try {
            val dir = File(path)
            val files = dir.listFiles()?.toList() ?: emptyList()
            Result.success(files)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fileExists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).exists()
    }

    suspend fun getFileSize(file: File): Long = withContext(Dispatchers.IO) {
        file.length()
    }
}
