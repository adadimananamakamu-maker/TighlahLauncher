package com.tighlah.launcher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "game_accounts")
data class GameAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accountType: String, // "microsoft", "offline"
    val username: String,
    val uuid: String = "",
    val accessToken: String = "", // Encrypted/hashed
    val refreshToken: String = "", // Encrypted/hashed
    val expiresAt: Long = 0L,
    val skinUrl: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun isExpired(): Boolean = System.currentTimeMillis() > expiresAt && accessToken.isNotEmpty()
}
