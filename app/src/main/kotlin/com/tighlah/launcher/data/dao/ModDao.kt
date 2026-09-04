package com.tighlah.launcher.data.dao

import androidx.room.*
import com.tighlah.launcher.data.model.Mod
import kotlinx.coroutines.flow.Flow

@Dao
interface ModDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mod: Mod): Long

    @Update
    suspend fun update(mod: Mod)

    @Delete
    suspend fun delete(mod: Mod)

    @Query("SELECT * FROM mods WHERE id = :id")
    suspend fun getModById(id: Int): Mod?

    @Query("SELECT * FROM mods WHERE instanceId = :instanceId ORDER BY name ASC")
    fun getModsByInstance(instanceId: Int): Flow<List<Mod>>

    @Query("SELECT * FROM mods WHERE instanceId = :instanceId AND isEnabled = 1 ORDER BY name ASC")
    suspend fun getEnabledModsByInstance(instanceId: Int): List<Mod>

    @Query("SELECT COUNT(*) FROM mods WHERE instanceId = :instanceId")
    suspend fun getModCount(instanceId: Int): Int

    @Query("DELETE FROM mods WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM mods WHERE instanceId = :instanceId")
    suspend fun deleteModsByInstance(instanceId: Int)

    @Query("UPDATE mods SET isEnabled = :enabled WHERE id = :id")
    suspend fun setModEnabled(id: Int, enabled: Boolean)

    @Query("SELECT * FROM mods WHERE instanceId = :instanceId AND filename = :filename")
    suspend fun getModByFilename(instanceId: Int, filename: String): Mod?
}
