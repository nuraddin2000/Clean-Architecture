package com.test.cleanarchitecture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: Application
    }
}