package com.tighlah.launcher.network.repository

import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.network.ModrinthApiService
import com.tighlah.launcher.network.model.ModrinthProject
import com.tighlah.launcher.network.model.ModrinthVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModrinthRepository(private val apiService: ModrinthApiService) {
    private val logger = "ModrinthRepo"

    suspend fun searchMods(
        query: String,
        gameVersion: String? = null,
        loader: String? = null,
        offset: Int = 0,
        limit: Int = 20
    ): Result<List<ModrinthProject>> = withContext(Dispatchers.IO) {
        try {
            var facets = "[[\"project_type:mod\""
            gameVersion?.let { facets += ", \"versions:$it\"" }
            loader?.let { facets += ", \"categories:$it\"" }
            facets += "]]"

            val result = apiService.searchMods(
                query = query,
                offset = offset,
                limit = limit,
                facets = facets
            )

            TighlahLogger.i(logger, "Found ${result.hits.size} mods on Modrinth")
            Result.success(result.hits)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to search mods", e)
            Result.failure(e)
        }
    }

    suspend fun getProjectVersions(
        projectId: String,
        gameVersion: String? = null,
        loader: String? = null
    ): Result<List<ModrinthVersion>> = withContext(Dispatchers.IO) {
        try {
            val versions = apiService.getProjectVersions(
                projectId = projectId,
                gameVersions = gameVersion,
                loaders = loader
            )
            Result.success(versions)
        } catch (e: Exception) {
            TighlahLogger.e(logger, "Failed to get project versions", e)
            Result.failure(e)
        }
    }

    suspend fun getPopularMods(
        gameVersion: String? = null,
        loader: String? = null,
        limit: Int = 20
    ): Result<List<ModrinthProject>> = withContext(Dispatchers.IO) {
        try {
            val result = searchMods(
                query = "",
                gameVersion = gameVersion,
                loader = loader,
                limit = limit
            )
            Result.success(result.getOrNull() ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
