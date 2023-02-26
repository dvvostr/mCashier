package ru.studiq.mcashier.model.classes


import android.app.Application
import android.content.Context
import android.content.res.Resources

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        instance = this
        res = resources
    }

    companion object {
        lateinit  var appContext: Context
        var instance: App? = null
            private set
        var res: Resources? = null
            private set
    }
}