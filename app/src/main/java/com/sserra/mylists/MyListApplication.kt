package com.sserra.mylists

import android.app.Application
import timber.log.Timber

class MyListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}