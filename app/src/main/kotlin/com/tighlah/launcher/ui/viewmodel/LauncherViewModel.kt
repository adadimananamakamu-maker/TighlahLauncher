package com.tighlah.launcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tighlah.launcher.core.launcher.LaunchConfiguration
import com.tighlah.launcher.core.launcher.MinecraftLauncher
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.data.model.GameAccount
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.model.LaunchLog
import com.tighlah.launcher.data.repository.LaunchLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LauncherViewModel(
    private val minecraftLauncher: MinecraftLauncher,
    private val launchLogRepository: LaunchLogRepository
) : ViewModel() {
    private val logger = "LauncherViewModel"

    private val _launchLogs = MutableStateFlow<List<LaunchLog>>(emptyList())
    val launchLogs: StateFlow<List<LaunchLog>> = _launchLogs.asStateFlow()

    private val _isLaunching = MutableStateFlow(false)
    val isLaunching: StateFlow<Boolean> = _isLaunching.asStateFlow()

    private val _launchOutput = MutableStateFlow<List<String>>(emptyList())
    val launchOutput: StateFlow<List<String>> = _launchOutput.asStateFlow()

    private val _launchError = MutableStateFlow<String?>(null)
    val launchError: StateFlow<String?> = _launchError.asStateFlow()

    fun launchInstance(
        instance: Instance,
        account: GameAccount,
        javaRuntime: String,
        classpath: List<String>,
        mainClass: String,
        gameArguments: List<String>,
        jvmArguments: List<String>
    ) {
        viewModelScope.launch {
            try {
                _isLaunching.value = true
                _launchOutput.value = emptyList()
                _launchError.value = null

                TighlahLogger.i(logger, "Launching instance: ${instance.name}")

                val configuration = LaunchConfiguration(
                    instance = instance,
                    account = account,
                    javaRuntime = javaRuntime,
                    classpath = classpath,
                    mainClass = mainClass,
                    gameArguments = gameArguments,
                    jvmArguments = jvmArguments
                )

                val result = minecraftLauncher.launch(
                    configuration,
                    onOutput = { line ->
                        _launchOutput.value = _launchOutput.value + line
                    },
                    onError = { line ->
                        _launchOutput.value = _launchOutput.value + "[ERROR] $line"
                    }
                )

                result.onSuccess { log ->
                    launchLogRepository.addLog(log)
                    TighlahLogger.i(logger, "Instance finished successfully")
                }.onFailure { exception ->
                    _launchError.value = exception.message ?: "Launch failed"
                    TighlahLogger.e(logger, "Launch failed", exception)
                }
            } finally {
                _isLaunching.value = false
            }
        }
    }

    fun loadLogs(instanceId: Int) {
        viewModelScope.launch {
            try {
                launchLogRepository.getLogsByInstance(instanceId).collect { logs ->
                    _launchLogs.value = logs
                }
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to load logs", e)
            }
        }
    }

    fun clearError() {
        _launchError.value = null
    }
}
