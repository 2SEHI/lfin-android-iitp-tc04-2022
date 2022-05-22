package com.lfin.android.iitp.lfin_android_iitp_tc04_2022

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    init{
        instance = this
    }

    companion object {
        lateinit var instance: MainApplication
        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }
}