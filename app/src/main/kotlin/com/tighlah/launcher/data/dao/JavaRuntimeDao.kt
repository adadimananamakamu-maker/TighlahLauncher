package com.tighlah.launcher.data.dao

import androidx.room.*
import com.tighlah.launcher.data.model.JavaRuntime
import kotlinx.coroutines.flow.Flow

@Dao
interface JavaRuntimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(runtime: JavaRuntime): Long

    @Update
    suspend fun update(runtime: JavaRuntime)

    @Delete
    suspend fun delete(runtime: JavaRuntime)

    @Query("SELECT * FROM java_runtimes WHERE id = :id")
    suspend fun getRuntimeById(id: Int): JavaRuntime?

    @Query("SELECT * FROM java_runtimes ORDER BY version DESC")
    fun getAllRuntimes(): Flow<List<JavaRuntime>>

    @Query("SELECT * FROM java_runtimes WHERE isValid = 1 ORDER BY version DESC")
    suspend fun getValidRuntimes(): List<JavaRuntime>

    @Query("DELETE FROM java_runtimes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE java_runtimes SET isValid = 0 WHERE id = :id")
    suspend fun markAsInvalid(id: Int)
}
