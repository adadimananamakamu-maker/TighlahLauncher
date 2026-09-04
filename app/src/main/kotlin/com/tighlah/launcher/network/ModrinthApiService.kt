package com.tighlah.launcher.network

import com.tighlah.launcher.network.model.ModrinthSearchResult
import com.tighlah.launcher.network.model.ModrinthVersion
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ModrinthApiService {
    @GET("v2/search")
    suspend fun searchMods(
        @Query("query") query: String,
        @Query("index") index: String = "relevance",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20,
        @Query("facets") facets: String = "[[\"project_type:mod\"]]"
    ): ModrinthSearchResult

    @GET("v2/project/{id}/versions")
    suspend fun getProjectVersions(
        @Path("id") projectId: String,
        @Query("game_versions") gameVersions: String? = null,
        @Query("loaders") loaders: String? = null
    ): List<ModrinthVersion>
}
