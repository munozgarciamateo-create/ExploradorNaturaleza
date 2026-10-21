package com.example.exploradornaturaleza.repository

import com.example.exploradornaturaleza.db.RazaFavoritaDao
import com.example.exploradornaturaleza.mapper.aEntity
import com.example.exploradornaturaleza.mapper.aModelo
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.RazaFavorita
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Repositorio local: traduce entre RazaFavoritaEntity (Room) y el modelo del dominio.
class FavoritasRepository(private val dao: RazaFavoritaDao) {

    fun observarFavoritas(): Flow<List<RazaFavorita>> =
        dao.observarTodas().map { lista -> lista.map { it.aModelo() } }

    fun esFavorita(nombre: String): Flow<Boolean> = dao.esFavorita(nombre)

    suspend fun guardar(raza: Raza) = dao.insertar(raza.aEntity())

    suspend fun quitar(nombre: String) = dao.eliminarPorNombre(nombre)
}
