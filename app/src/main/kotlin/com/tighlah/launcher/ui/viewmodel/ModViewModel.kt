package com.tighlah.launcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.core.mod.ModManager
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.model.Mod
import com.tighlah.launcher.data.repository.ModRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ModViewModel(
    private val modRepository: ModRepository,
    private val modManager: ModManager
) : ViewModel() {
    private val logger = "ModViewModel"

    private val _mods = MutableStateFlow<List<Mod>>(emptyList())
    val mods: StateFlow<List<Mod>> = _mods.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _importProgress = MutableStateFlow<String>("")
    val importProgress: StateFlow<String> = _importProgress.asStateFlow()

    private var currentInstanceId: Int = -1

    fun loadModsForInstance(instanceId: Int) {
        currentInstanceId = instanceId
        viewModelScope.launch {
            try {
                _isLoading.value = true
                modRepository.getModsByInstance(instanceId).collect { modsList ->
                    _mods.value = modsList
                }
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to load mods", e)
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun importMod(instance: Instance, sourceFile: File) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = modManager.importMod(instance, sourceFile) { progress ->
                    _importProgress.value = progress
                }
                
                result.onSuccess { mod ->
                    modRepository.addMod(mod)
                    TighlahLogger.i(logger, "Mod imported: ${mod.name}")
                    loadModsForInstance(instance.id)
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Failed to import mod"
                    TighlahLogger.e(logger, "Import failed", exception)
                }
            } finally {
                _isLoading.value = false
                _importProgress.value = ""
            }
        }
    }

    fun deleteMod(instance: Instance, mod: Mod) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                modManager.deleteMod(instance, mod).onSuccess {
                    modRepository.deleteModById(mod.id)
                    TighlahLogger.i(logger, "Mod deleted: ${mod.name}")
                    loadModsForInstance(instance.id)
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Failed to delete mod"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleMod(instance: Instance, mod: Mod, enable: Boolean) {
        viewModelScope.launch {
            try {
                modManager.toggleMod(instance, mod, enable).onSuccess { newFile ->
                    val updatedMod = mod.copy(
                        filePath = newFile.absolutePath,
                        isEnabled = enable,
                        updatedAt = System.currentTimeMillis()
                    )
                    modRepository.updateMod(updatedMod)
                    TighlahLogger.i(logger, "Mod toggled: ${mod.name} -> ${if (enable) "ENABLED" else "DISABLED"}")
                    loadModsForInstance(instance.id)
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Failed to toggle mod"
                }
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Toggle mod failed", e)
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
