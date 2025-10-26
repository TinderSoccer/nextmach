package com.nextmatch.app

import android.app.Application
import com.nextmatch.app.data.database.NextMatchDatabase

class NextMatchApplication : Application() {
    companion object {
        lateinit var database: NextMatchDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = NextMatchDatabase.getDatabase(this)
    }
}