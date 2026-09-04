package com.tighlah.launcher

import android.app.Application
import com.tighlah.launcher.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TighlahApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin DI
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TighlahApplication)
            modules(appModules)
        }
    }
}
