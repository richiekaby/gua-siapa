package net.larntech.guasiapa

import android.app.Application

class App : Application() {

companion object {
    private var sApp: App? = null

    fun getInstance(): App? {
        return sApp
    }
}

    override fun onCreate() {
        sApp = this
        super.onCreate()

    }
}