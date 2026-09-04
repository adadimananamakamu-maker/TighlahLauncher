package com.tighlah.launcher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
@Entity(tableName = "java_runtimes")
data class JavaRuntime(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val path: String,
    val version: String = "",
    val architecture: String = "", // "x86", "x86_64", "arm", "arm64"
    val isValid: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getExecutablePath(): File = File(path)
    fun isAvailable(): Boolean = getExecutablePath().exists() && isValid
}
