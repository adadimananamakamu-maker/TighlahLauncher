package com.tighlah.launcher.data.repository

import com.tighlah.launcher.data.dao.JavaRuntimeDao
import com.tighlah.launcher.data.model.JavaRuntime
import kotlinx.coroutines.flow.Flow

class JavaRuntimeRepository(private val javaRuntimeDao: JavaRuntimeDao) {
    
    suspend fun addRuntime(runtime: JavaRuntime): Long {
        return javaRuntimeDao.insert(runtime)
    }

    suspend fun updateRuntime(runtime: JavaRuntime) {
        javaRuntimeDao.update(runtime)
    }

    suspend fun deleteRuntime(runtime: JavaRuntime) {
        javaRuntimeDao.delete(runtime)
    }

    suspend fun deleteRuntimeById(id: Int) {
        javaRuntimeDao.deleteById(id)
    }

    suspend fun getRuntimeById(id: Int): JavaRuntime? {
        return javaRuntimeDao.getRuntimeById(id)
    }

    fun getAllRuntimes(): Flow<List<JavaRuntime>> {
        return javaRuntimeDao.getAllRuntimes()
    }

    suspend fun getValidRuntimes(): List<JavaRuntime> {
        return javaRuntimeDao.getValidRuntimes()
    }

    suspend fun markAsInvalid(id: Int) {
        javaRuntimeDao.markAsInvalid(id)
    }
}
