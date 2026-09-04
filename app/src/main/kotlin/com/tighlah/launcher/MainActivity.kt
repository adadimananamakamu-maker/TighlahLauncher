package com.tighlah.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tighlah.launcher.data.model.Instance
import com.tighlah.launcher.ui.screen.InstanceDetailScreen
import com.tighlah.launcher.ui.screen.InstanceListScreen
import com.tighlah.launcher.ui.screen.ModManagerScreen
import com.tighlah.launcher.ui.theme.TighlahLauncherTheme
import com.tighlah.launcher.ui.viewmodel.InstanceViewModel
import com.tighlah.launcher.ui.viewmodel.LauncherViewModel
import com.tighlah.launcher.ui.viewmodel.ModViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val instanceViewModel: InstanceViewModel by viewModel()
    private val modViewModel: ModViewModel by viewModel()
    private val launcherViewModel: LauncherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TighlahLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen = remember { mutableStateOf<Screen>(Screen.InstanceList) }
                    val selectedInstance = remember { mutableStateOf<Instance?>(null) }

                    when (val screen = currentScreen.value) {
                        is Screen.InstanceList -> {
                            InstanceListScreen(
                                viewModel = instanceViewModel,
                                onInstanceSelected = { instance ->
                                    selectedInstance.value = instance
                                    launcherViewModel.loadLogs(instance.id)
                                    currentScreen.value = Screen.InstanceDetail(instance)
                                },
                                onCreateInstance = {
                                    // TODO: Show create instance dialog
                                },
                                onSettings = {
                                    // TODO: Show settings screen
                                }
                            )
                        }

                        is Screen.InstanceDetail -> {
                            InstanceDetailScreen(
                                instance = screen.instance,
                                viewModel = launcherViewModel,
                                onBack = {
                                    currentScreen.value = Screen.InstanceList
                                },
                                onPlayClick = {
                                    // TODO: Implement launch logic
                                },
                                onModsClick = {
                                    modViewModel.loadModsForInstance(screen.instance.id)
                                    currentScreen.value = Screen.ModManager(screen.instance)
                                },
                                onSettingsClick = {
                                    // TODO: Show instance settings
                                }
                            )
                        }

                        is Screen.ModManager -> {
                            ModManagerScreen(
                                instance = screen.instance,
                                viewModel = modViewModel,
                                onBack = {
                                    currentScreen.value = Screen.InstanceDetail(screen.instance)
                                },
                                onAddMod = { instance ->
                                    // TODO: Show file picker for mod selection
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class Screen {
        object InstanceList : Screen()
        data class InstanceDetail(val instance: Instance) : Screen()
        data class ModManager(val instance: Instance) : Screen()
    }
}
