package com.example.exploradornaturaleza.db

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "razas_favoritas": una fila por cada raza marcada como favorita.
@Entity(tableName = "razas_favoritas")
data class RazaFavoritaEntity(
    @PrimaryKey val nombre: String,   // ej: "bulldog" (no se puede repetir)
    val cantidadSubrazas: Int         // ej: 3
)
