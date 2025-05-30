package com.ebc.misfinanzas.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movimiento::class], version = 2) // Aumenta la versión si hiciste cambios
abstract class AppDatabase : RoomDatabase() {
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mis_finanzas_db"
                )
                    .fallbackToDestructiveMigration() // Borra y recrea la BD si cambió el schema RECUERDA CAMBIARLO AL FINAL
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


