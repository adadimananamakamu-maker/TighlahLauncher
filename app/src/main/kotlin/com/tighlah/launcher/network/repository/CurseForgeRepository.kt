package com.tighlah.launcher.network.repository

import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.network.CurseForgeApiService
import com.tighlah.launcher.network.model.CurseForgeProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurseForgeRepository(
    private val apiService: CurseForgeApiService,
    private val apiKey: String
) {
    private val logger = "CurseForgeRepo"

    suspend fun searchMods(
        query: String,
        gameVersion: String? = null,
        modLoader: String? = null,
        offset: Int = 0
    ): Result<List<CurseForgeProject>> = withContext(Dispatchers.IO) {
        try {
            val modLoaderType = when (modLoader?.lowercase()) {
                "forge" -> 1
                "fabric" -> 4
                "neoforge" -> 5
                else -> null
            }

            val result = apiService.searchModsFiltered(
                apiKey = apiKey,
                query = query,
                gameVersion = gameVersion,
                modLoaderType = modLoaderType,
                index = offset
            )

            TighlahLogger.i(logger, "Found ${result.data.size} mods on CurseForge")
            Result.success(result.data)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to search mods", e)
            Result.failure(e)
        }
    }

    suspend fun getPopularMods(
        gameVersion: String? = null,
        modLoader: String? = null,
        limit: Int = 20
    ): Result<List<CurseForgeProject>> = withContext(Dispatchers.IO) {
        try {
            val result = searchMods("*", gameVersion, modLoader)
            val mods = result.getOrNull()?.take(limit) ?: emptyList()
            Result.success(mods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
