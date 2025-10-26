package com.nextmatch.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nextmatch.app.data.dao.EquipoFavoritoDao
import com.nextmatch.app.data.dao.HistorialBusquedaDao
import com.nextmatch.app.data.dao.ReservaDao
import com.nextmatch.app.data.dao.UsuarioDao
import com.nextmatch.app.data.entities.EquipoFavoritoEntity
import com.nextmatch.app.data.entities.HistorialBusquedaEntity
import com.nextmatch.app.data.entities.ReservaEntity
import com.nextmatch.app.data.entities.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        EquipoFavoritoEntity::class,
        HistorialBusquedaEntity::class,
        ReservaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NextMatchDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun equipoFavoritoDao(): EquipoFavoritoDao
    abstract fun historialBusquedaDao(): HistorialBusquedaDao
    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile
        private var instance: NextMatchDatabase? = null

        fun getDatabase(context: Context): NextMatchDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    NextMatchDatabase::class.java,
                    "nextmatch_database"
                )
                    .build()
                instance = db
                db
            }
        }
    }
}