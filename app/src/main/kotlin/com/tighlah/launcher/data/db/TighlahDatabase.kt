package com.tighlah.launcher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tighlah.launcher.data.dao.*
import com.tighlah.launcher.data.model.*

@Database(
    entities = [
        Instance::class,
        Mod::class,
        JavaRuntime::class,
        GameAccount::class,
        LaunchLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TighlahDatabase : RoomDatabase() {
    abstract fun instanceDao(): InstanceDao
    abstract fun modDao(): ModDao
    abstract fun javaRuntimeDao(): JavaRuntimeDao
    abstract fun gameAccountDao(): GameAccountDao
    abstract fun launchLogDao(): LaunchLogDao
}
