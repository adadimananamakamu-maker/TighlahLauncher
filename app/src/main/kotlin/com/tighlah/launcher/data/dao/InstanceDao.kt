package com.tighlah.launcher.data.dao

import androidx.room.*
import com.tighlah.launcher.data.model.Instance
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(instance: Instance): Long

    @Update
    suspend fun update(instance: Instance)

    @Delete
    suspend fun delete(instance: Instance)

    @Query("SELECT * FROM instances WHERE id = :id")
    suspend fun getInstanceById(id: Int): Instance?

    @Query("SELECT * FROM instances ORDER BY updatedAt DESC")
    fun getAllInstances(): Flow<List<Instance>>

    @Query("SELECT * FROM instances WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultInstance(): Instance?

    @Query("DELETE FROM instances WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE instances SET isDefault = 0")
    suspend fun clearDefaultInstance()

    @Query("UPDATE instances SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultInstance(id: Int)

    @Query("SELECT COUNT(*) FROM instances")
    suspend fun getInstanceCount(): Int
}
