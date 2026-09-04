package com.tighlah.launcher.data.repository

import com.tighlah.launcher.data.dao.InstanceDao
import com.tighlah.launcher.data.model.Instance
import kotlinx.coroutines.flow.Flow

class InstanceRepository(private val instanceDao: InstanceDao) {
    
    suspend fun createInstance(instance: Instance): Long {
        return instanceDao.insert(instance)
    }

    suspend fun updateInstance(instance: Instance) {
        instanceDao.update(instance)
    }

    suspend fun deleteInstance(instance: Instance) {
        instanceDao.delete(instance)
    }

    suspend fun deleteInstanceById(id: Int) {
        instanceDao.deleteById(id)
    }

    suspend fun getInstanceById(id: Int): Instance? {
        return instanceDao.getInstanceById(id)
    }

    fun getAllInstances(): Flow<List<Instance>> {
        return instanceDao.getAllInstances()
    }

    suspend fun getDefaultInstance(): Instance? {
        return instanceDao.getDefaultInstance()
    }

    suspend fun setDefaultInstance(id: Int) {
        instanceDao.clearDefaultInstance()
        instanceDao.setDefaultInstance(id)
    }

    suspend fun duplicateInstance(sourceInstance: Instance, newName: String): Long {
        val newInstance = sourceInstance.copy(
            id = 0,
            name = newName,
            gameDirectory = sourceInstance.gameDirectory.replace(sourceInstance.name, newName),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isDefault = false
        )
        return createInstance(newInstance)
    }

    suspend fun getInstanceCount(): Int {
        return instanceDao.getInstanceCount()
    }
}
