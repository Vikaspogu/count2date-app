package com.vikaspogu.count2date

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Count2DateApplication: Application() {

    private lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}