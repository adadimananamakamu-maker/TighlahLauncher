package com.tighlah.launcher.data.repository

import com.tighlah.launcher.data.dao.GameAccountDao
import com.tighlah.launcher.data.model.GameAccount
import kotlinx.coroutines.flow.Flow

class GameAccountRepository(private val gameAccountDao: GameAccountDao) {
    
    suspend fun addAccount(account: GameAccount): Long {
        return gameAccountDao.insert(account)
    }

    suspend fun updateAccount(account: GameAccount) {
        gameAccountDao.update(account)
    }

    suspend fun deleteAccount(account: GameAccount) {
        gameAccountDao.delete(account)
    }

    suspend fun deleteAccountById(id: Int) {
        gameAccountDao.deleteById(id)
    }

    suspend fun getAccountById(id: Int): GameAccount? {
        return gameAccountDao.getAccountById(id)
    }

    fun getAllAccounts(): Flow<List<GameAccount>> {
        return gameAccountDao.getAllAccounts()
    }

    suspend fun getActiveAccount(): GameAccount? {
        return gameAccountDao.getActiveAccount()
    }

    suspend fun getAccountByUsername(username: String): GameAccount? {
        return gameAccountDao.getAccountByUsername(username)
    }

    suspend fun setActiveAccount(id: Int) {
        gameAccountDao.clearActiveAccount()
        gameAccountDao.setActiveAccount(id)
    }
}
