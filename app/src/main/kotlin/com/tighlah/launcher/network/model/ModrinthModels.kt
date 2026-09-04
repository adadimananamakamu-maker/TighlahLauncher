package com.tighlah.launcher.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProject(
    val id: String,
    val slug: String,
    val name: String,
    val description: String = "",
    @SerialName("project_type")
    val projectType: String = "mod",
    val icon_url: String? = null,
    val author: String = "",
    @SerialName("game_versions")
    val gameVersions: List<String> = emptyList(),
    val loaders: List<String> = emptyList(),
    val downloads: Int = 0
)

@Serializable
data class ModrinthVersion(
    val id: String,
    @SerialName("project_id")
    val projectId: String,
    val name: String,
    @SerialName("version_number")
    val versionNumber: String,
    @SerialName("game_versions")
    val gameVersions: List<String> = emptyList(),
    val loaders: List<String> = emptyList(),
    val files: List<ModrinthFile> = emptyList()
)

@Serializable
data class ModrinthFile(
    val url: String,
    val filename: String,
    val size: Long = 0L,
    val hashes: Map<String, String> = emptyMap()
)

@Serializable
data class ModrinthSearchResult(
    val hits: List<ModrinthProject> = emptyList(),
    val offset: Int = 0,
    val limit: Int = 0,
    @SerialName("total_hits")
    val totalHits: Int = 0
)
