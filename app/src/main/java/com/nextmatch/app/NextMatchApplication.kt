package com.nextmatch.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.nextmatch.app.data.database.NextMatchDatabase
import org.osmdroid.config.Configuration

class NextMatchApplication : Application() {
    companion object {
        lateinit var database: NextMatchDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        Configuration.getInstance().userAgentValue = applicationContext.packageName
        database = NextMatchDatabase.getDatabase(this)
    }
}
