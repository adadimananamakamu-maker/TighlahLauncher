package com.tighlah.launcher.data.dao

import androidx.room.*
import com.tighlah.launcher.data.model.GameAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface GameAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: GameAccount): Long

    @Update
    suspend fun update(account: GameAccount)

    @Delete
    suspend fun delete(account: GameAccount)

    @Query("SELECT * FROM game_accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): GameAccount?

    @Query("SELECT * FROM game_accounts ORDER BY updatedAt DESC")
    fun getAllAccounts(): Flow<List<GameAccount>>

    @Query("SELECT * FROM game_accounts WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveAccount(): GameAccount?

    @Query("SELECT * FROM game_accounts WHERE username = :username")
    suspend fun getAccountByUsername(username: String): GameAccount?

    @Query("DELETE FROM game_accounts WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE game_accounts SET isActive = 0")
    suspend fun clearActiveAccount()

    @Query("UPDATE game_accounts SET isActive = 1 WHERE id = :id")
    suspend fun setActiveAccount(id: Int)
}
