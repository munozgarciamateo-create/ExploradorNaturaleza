package com.example.exploradornaturaleza.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RazaFavoritaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun razaFavoritaDao(): RazaFavoritaDao

    companion object {
        // @Volatile: todos los hilos ven siempre el valor más reciente.
        @Volatile
        private var instancia: AppDatabase? = null

        // Singleton: UNA sola base de datos para toda la app.
        // synchronized evita que dos hilos la creen al mismo tiempo.
        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext, // nunca el contexto de una Activity
                    AppDatabase::class.java,
                    "explorador.db"
                ).build().also { instancia = it }
            }
    }
}
