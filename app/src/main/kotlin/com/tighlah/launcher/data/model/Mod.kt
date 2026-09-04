package com.tighlah.launcher.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "mods",
    foreignKeys = [
        ForeignKey(
            entity = Instance::class,
            parentColumns = ["id"],
            childColumns = ["instanceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("instanceId")]
)
data class Mod(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val instanceId: Int,
    val name: String,
    val filename: String,
    val filePath: String,
    val version: String = "",
    val fileSize: Long = 0L,
    val isEnabled: Boolean = true,
    val modLoader: String = "", // "forge", "fabric", "neoforge", etc
    val minecraftVersion: String = "",
    val description: String = "",
    val author: String = "",
    val iconPath: String = "",
    val sourceUrl: String = "", // URL to mod provider (CurseForge, Modrinth, etc)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getDisabledFilename(): String = "$filename.disabled"
}
