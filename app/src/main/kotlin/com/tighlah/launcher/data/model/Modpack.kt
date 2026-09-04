package com.tighlah.launcher.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModpackManifest(
    val name: String,
    val description: String = "",
    @SerialName("minecraft_version")
    val minecraftVersion: String,
    val loader: String, // "forge", "fabric", "neoforge"
    @SerialName("loader_version")
    val loaderVersion: String,
    val author: String = "",
    val version: String = "1.0.0",
    @SerialName("jvm_args")
    val jvmArguments: String = "",
    @SerialName("ram_allocation")
    val ramAllocation: Int = 2048
)

@Serializable
data class ModpackImportData(
    val manifest: ModpackManifest,
    val modsCount: Int = 0,
    val configCount: Int = 0,
    val resourcePacksCount: Int = 0,
    val shadersCount: Int = 0
)
