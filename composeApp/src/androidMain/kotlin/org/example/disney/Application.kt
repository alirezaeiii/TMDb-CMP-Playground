package org.example.disney

import di.initKoin
import utils.initializeNapier

class Application: android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initializeNapier()
    }
}