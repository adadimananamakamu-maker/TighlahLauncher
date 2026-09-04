package com.tighlah.launcher.data.dao

import androidx.room.*
import com.tighlah.launcher.data.model.LaunchLog
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: LaunchLog): Long

    @Update
    suspend fun update(log: LaunchLog)

    @Delete
    suspend fun delete(log: LaunchLog)

    @Query("SELECT * FROM launch_logs WHERE id = :id")
    suspend fun getLogById(id: Int): LaunchLog?

    @Query("SELECT * FROM launch_logs WHERE instanceId = :instanceId ORDER BY launchedAt DESC")
    fun getLogsByInstance(instanceId: Int): Flow<List<LaunchLog>>

    @Query("SELECT * FROM launch_logs WHERE instanceId = :instanceId ORDER BY launchedAt DESC LIMIT :limit")
    suspend fun getRecentLogs(instanceId: Int, limit: Int = 10): List<LaunchLog>

    @Query("DELETE FROM launch_logs WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM launch_logs WHERE instanceId = :instanceId")
    suspend fun deleteLogsByInstance(instanceId: Int)
}
