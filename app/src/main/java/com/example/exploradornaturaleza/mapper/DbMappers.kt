package com.example.exploradornaturaleza.mapper

import com.example.exploradornaturaleza.db.RazaFavoritaEntity
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.RazaFavorita

// Modelo del dominio -> fila de la tabla
fun Raza.aEntity(): RazaFavoritaEntity =
    RazaFavoritaEntity(nombre = nombre, cantidadSubrazas = cantidadSubrazas)

// Fila de la tabla -> modelo del dominio
fun RazaFavoritaEntity.aModelo(): RazaFavorita =
    RazaFavorita(nombre = nombre, cantidadSubrazas = cantidadSubrazas)
