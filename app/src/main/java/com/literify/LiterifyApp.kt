package com.literify

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LiterifyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}