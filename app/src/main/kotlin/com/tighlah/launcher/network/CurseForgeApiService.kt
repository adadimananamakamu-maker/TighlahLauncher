package com.tighlah.launcher.network

import com.tighlah.launcher.network.model.CurseForgeSearchResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurseForgeApiService {
    @GET("mods/search")
    suspend fun searchMods(
        @Header("x-api-key") apiKey: String,
        @Query("searchFilter") query: String,
        @Query("gameId") gameId: Int = 432,  // Minecraft
        @Query("classId") classId: Int = 6,  // Mods
        @Query("index") index: Int = 0,
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortField") sortField: Int = 2, // Popularity
        @Query("sortOrder") sortOrder: String = "desc"
    ): CurseForgeSearchResult

    @GET("mods/search")
    suspend fun searchModsFiltered(
        @Header("x-api-key") apiKey: String,
        @Query("searchFilter") query: String,
        @Query("gameVersion") gameVersion: String?,
        @Query("modLoaderType") modLoaderType: Int?,
        @Query("gameId") gameId: Int = 432,
        @Query("classId") classId: Int = 6,
        @Query("index") index: Int = 0,
        @Query("pageSize") pageSize: Int = 20
    ): CurseForgeSearchResult
}
