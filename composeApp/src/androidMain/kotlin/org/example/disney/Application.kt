package org.example.disney

import di.initKoin
import org.koin.android.ext.koin.androidContext
import utils.initializeNapier

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Application)
        }
        initializeNapier()
    }
}