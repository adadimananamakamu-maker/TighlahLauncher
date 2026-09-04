package com.tighlah.launcher.data.repository

import com.tighlah.launcher.data.dao.LaunchLogDao
import com.tighlah.launcher.data.model.LaunchLog
import kotlinx.coroutines.flow.Flow

class LaunchLogRepository(private val launchLogDao: LaunchLogDao) {
    
    suspend fun addLog(log: LaunchLog): Long {
        return launchLogDao.insert(log)
    }

    suspend fun updateLog(log: LaunchLog) {
        launchLogDao.update(log)
    }

    suspend fun deleteLog(log: LaunchLog) {
        launchLogDao.delete(log)
    }

    suspend fun deleteLogById(id: Int) {
        launchLogDao.deleteById(id)
    }

    suspend fun getLogById(id: Int): LaunchLog? {
        return launchLogDao.getLogById(id)
    }

    fun getLogsByInstance(instanceId: Int): Flow<List<LaunchLog>> {
        return launchLogDao.getLogsByInstance(instanceId)
    }

    suspend fun getRecentLogs(instanceId: Int, limit: Int = 10): List<LaunchLog> {
        return launchLogDao.getRecentLogs(instanceId, limit)
    }

    suspend fun deleteLogsByInstance(instanceId: Int) {
        launchLogDao.deleteLogsByInstance(instanceId)
    }
}
