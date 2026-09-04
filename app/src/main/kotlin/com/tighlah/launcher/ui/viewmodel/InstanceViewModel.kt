package com.tighlah.launcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.data.repository.InstanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class InstanceViewModel(private val instanceRepository: InstanceRepository) : ViewModel() {
    private val logger = "InstanceViewModel"

    private val _instances = MutableStateFlow<List<Instance>>(emptyList())
    val instances: StateFlow<List<Instance>> = _instances.asStateFlow()

    private val _selectedInstance = MutableStateFlow<Instance?>(null)
    val selectedInstance: StateFlow<Instance?> = _selectedInstance.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInstances()
    }

    private fun loadInstances() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                instanceRepository.getAllInstances().collect { instances ->
                    _instances.value = instances
                    if (_selectedInstance.value == null && instances.isNotEmpty()) {
                        _selectedInstance.value = instances.first()
                    }
                }
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to load instances", e)
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createInstance(
        name: String,
        minecraftVersion: String,
        loader: String,
        loaderVersion: String,
        baseGameDirectory: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val gameDirectory = File(baseGameDirectory, name)
                gameDirectory.mkdirs()

                val instance = Instance(
                    name = name,
                    minecraftVersion = minecraftVersion,
                    loader = loader,
                    loaderVersion = loaderVersion,
                    gameDirectory = gameDirectory.absolutePath
                )

                instanceRepository.createInstance(instance)
                TighlahLogger.i(logger, "Instance created: $name")
                loadInstances()
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to create instance", e)
                _error.value = e.message ?: "Failed to create instance"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateInstance(instance: Instance) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                instanceRepository.updateInstance(instance)
                _selectedInstance.value = instance
                TighlahLogger.i(logger, "Instance updated: ${instance.name}")
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to update instance", e)
                _error.value = e.message ?: "Failed to update instance"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteInstance(instance: Instance) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                instanceRepository.deleteInstance(instance)
                if (_selectedInstance.value?.id == instance.id) {
                    _selectedInstance.value = null
                }
                TighlahLogger.i(logger, "Instance deleted: ${instance.name}")
                loadInstances()
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to delete instance", e)
                _error.value = e.message ?: "Failed to delete instance"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun duplicateInstance(source: Instance, newName: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                instanceRepository.duplicateInstance(source, newName)
                TighlahLogger.i(logger, "Instance duplicated: ${source.name} -> $newName")
                loadInstances()
            } catch (e: Exception) {
                TighlahLogger.e(logger, "Failed to duplicate instance", e)
                _error.value = e.message ?: "Failed to duplicate instance"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectInstance(instance: Instance) {
        _selectedInstance.value = instance
    }

    fun clearError() {
        _error.value = null
    }
}
