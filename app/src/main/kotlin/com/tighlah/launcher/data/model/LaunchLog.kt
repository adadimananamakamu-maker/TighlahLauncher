package com.tighlah.launcher.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "launch_logs",
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
data class LaunchLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val instanceId: Int,
    val exitCode: Int = 0,
    val output: String = "",
    val error: String = "",
    val duration: Long = 0L, // milliseconds
    val launchedAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0L
)
