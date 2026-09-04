package com.tighlah.launcher.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurseForgeProject(
    val id: Int,
    val name: String,
    val slug: String,
    val summary: String = "",
    val logo: CurseForgeLogo? = null,
    val authors: List<CurseForgeAuthor> = emptyList(),
    @SerialName("gameVersionLatestFiles")
    val gameVersionLatestFiles: List<CurseForgeFile> = emptyList(),
    val downloadCount: Long = 0L
)

@Serializable
data class CurseForgeLogo(
    val url: String? = null,
    val thumbnailUrl: String? = null
)

@Serializable
data class CurseForgeAuthor(
    val id: Int,
    val name: String,
    val url: String = ""
)

@Serializable
data class CurseForgeFile(
    val id: Long,
    val gameVersion: String = "",
    val filename: String = "",
    val releaseType: String = "release",
    val downloadUrl: String? = null,
    val fileLength: Long = 0L
)

@Serializable
data class CurseForgeSearchResult(
    val data: List<CurseForgeProject> = emptyList(),
    val pagination: CurseForgePagination? = null
)

@Serializable
data class CurseForgePagination(
    val index: Int = 0,
    val pageSize: Int = 0,
    val resultCount: Int = 0,
    val totalCount: Int = 0
)
