package org.example.disney

import utils.initializeNapier

class Application: android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initializeNapier()
    }
}