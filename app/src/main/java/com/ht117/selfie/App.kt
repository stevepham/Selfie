package com.ht117.selfie

import android.app.Application
import com.ht117.selfie.data.dataModule
import com.ht117.selfie.ui.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, dataModule)
        }
    }
}
