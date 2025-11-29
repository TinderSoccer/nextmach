package com.nextmatch.app

import android.app.Application
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

class NextMatchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        Configuration.getInstance().userAgentValue = applicationContext.packageName
    }
}
