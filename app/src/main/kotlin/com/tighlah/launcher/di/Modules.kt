package com.tighlah.launcher.di

import android.content.Context
import androidx.room.Room
import com.tighlah.launcher.core.file.FileManager
import com.tighlah.launcher.core.launcher.MinecraftLauncher
import com.tighlah.launcher.core.launcher.MinecraftLauncherImpl
import com.tighlah.launcher.core.logging.TighlahLogger
import com.tighlah.launcher.core.mod.ModManager
import com.tighlah.launcher.core.modpack.ModpackManager
import com.tighlah.launcher.core.runtime.JavaRuntimeManager
import com.tighlah.launcher.data.db.TighlahDatabase
import com.tighlah.launcher.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<TighlahDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TighlahDatabase::class.java,
            "tighlah_launcher.db"
        ).build()
    }

    single { get<TighlahDatabase>().instanceDao() }
    single { get<TighlahDatabase>().modDao() }
    single { get<TighlahDatabase>().javaRuntimeDao() }
    single { get<TighlahDatabase>().gameAccountDao() }
    single { get<TighlahDatabase>().launchLogDao() }
}

val repositoryModule = module {
    single { InstanceRepository(get()) }
    single { ModRepository(get()) }
    single { JavaRuntimeRepository(get()) }
    single { GameAccountRepository(get()) }
    single { LaunchLogRepository(get()) }
}

val coreModule = module {
    single { FileManager(androidContext()) }
    single<MinecraftLauncher> { MinecraftLauncherImpl(androidContext()) }
    single { ModManager(androidContext(), get()) }
    single { ModpackManager(androidContext(), get()) }
    single { JavaRuntimeManager(androidContext()) }
}

val appModules = listOf(
    databaseModule,
    repositoryModule,
    coreModule
)
