package com.example

import android.app.Application
import timber.log.Timber

class ChatApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        //AndroidSmackInitializer.initialize(this);
        Timber.plant(Timber.DebugTree())
    }
}