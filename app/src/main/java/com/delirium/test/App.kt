package com.delirium.test

import android.app.Application
import com.yandex.mapkit.MapKitFactory


const val API_KEY = "df1bd91c-f27f-48dc-b230-3d75656da74b"

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(this)
    }
}