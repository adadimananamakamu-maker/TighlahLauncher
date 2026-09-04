package com.tighlah.launcher.data.repository

import com.tighlah.launcher.data.dao.ModDao
import com.tighlah.launcher.data.model.Mod
import kotlinx.coroutines.flow.Flow

class ModRepository(private val modDao: ModDao) {
    
    suspend fun addMod(mod: Mod): Long {
        return modDao.insert(mod)
    }

    suspend fun updateMod(mod: Mod) {
        modDao.update(mod)
    }

    suspend fun deleteMod(mod: Mod) {
        modDao.delete(mod)
    }

    suspend fun deleteModById(id: Int) {
        modDao.deleteById(id)
    }

    suspend fun getModById(id: Int): Mod? {
        return modDao.getModById(id)
    }

    fun getModsByInstance(instanceId: Int): Flow<List<Mod>> {
        return modDao.getModsByInstance(instanceId)
    }

    suspend fun getEnabledModsByInstance(instanceId: Int): List<Mod> {
        return modDao.getEnabledModsByInstance(instanceId)
    }

    suspend fun enableMod(id: Int) {
        modDao.setModEnabled(id, true)
    }

    suspend fun disableMod(id: Int) {
        modDao.setModEnabled(id, false)
    }

    suspend fun getModCount(instanceId: Int): Int {
        return modDao.getModCount(instanceId)
    }

    suspend fun deleteModsByInstance(instanceId: Int) {
        modDao.deleteModsByInstance(instanceId)
    }

    suspend fun getModByFilename(instanceId: Int, filename: String): Mod? {
        return modDao.getModByFilename(instanceId, filename)
    }
}
