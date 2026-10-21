package com.example.exploradornaturaleza.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RazaFavoritaDao {

    // Flow: se queda escuchando; Room vuelve a emitir cada vez que la tabla cambia.
    @Query("SELECT * FROM razas_favoritas ORDER BY nombre ASC")
    fun observarTodas(): Flow<List<RazaFavoritaEntity>>

    // Flow<Boolean>: el botón del detalle cambia solo al guardar o quitar.
    @Query("SELECT EXISTS(SELECT 1 FROM razas_favoritas WHERE nombre = :nombre)")
    fun esFavorita(nombre: String): Flow<Boolean>

    // suspend: escribe una vez sin congelar la pantalla.
    // REPLACE: si ya existe esa raza, la reemplaza en vez de lanzar un error.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(raza: RazaFavoritaEntity)

    // Quitar de favoritas con @Query DELETE (lo pide el enunciado).
    @Query("DELETE FROM razas_favoritas WHERE nombre = :nombre")
    suspend fun eliminarPorNombre(nombre: String)
}
