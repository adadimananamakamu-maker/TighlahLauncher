package com.tighlah.launcher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
@Entity(tableName = "instances")
data class Instance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val minecraftVersion: String,
    val loader: String, // "vanilla", "forge", "fabric", "neoforge"
    val loaderVersion: String = "",
    val gameDirectory: String,
    val javaRuntime: String = "",
    val ramAllocation: Int = 2048, // MB
    val jvmArguments: String = "",
    val resolution: String = "1280x720",
    val isFullscreen: Boolean = false,
    val rendererType: String = "opengl", // "opengl", "vulkan"
    val environmentVariables: String = "", // JSON string
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false
) {
    fun getModsDirectory(): File = File(gameDirectory, "mods")
    fun getConfigDirectory(): File = File(gameDirectory, "config")
    fun getResourcePacksDirectory(): File = File(gameDirectory, "resourcepacks")
    fun getShadersDirectory(): File = File(gameDirectory, "shaderpacks")
    fun getLogsDirectory(): File = File(gameDirectory, "logs")
    fun getScreenshotsDirectory(): File = File(gameDirectory, "screenshots")
}
