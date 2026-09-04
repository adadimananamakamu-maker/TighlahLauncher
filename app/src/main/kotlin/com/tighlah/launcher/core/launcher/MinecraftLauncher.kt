package com.tighlah.launcher.core.launcher

import com.tighlah.launcher.data.model.GameAccount
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.model.LaunchLog
import java.io.File

data class LaunchConfiguration(
    val instance: Instance,
    val account: GameAccount,
    val javaRuntime: String,
    val classpath: List<String>,
    val mainClass: String,
    val gameArguments: List<String>,
    val jvmArguments: List<String>,
    val environmentVariables: Map<String, String> = emptyMap()
)

interface MinecraftLauncher {
    suspend fun launch(
        configuration: LaunchConfiguration,
        onOutput: (String) -> Unit,
        onError: (String) -> Unit
    ): Result<LaunchLog>

    suspend fun validateConfiguration(configuration: LaunchConfiguration): Result<Boolean>
}
