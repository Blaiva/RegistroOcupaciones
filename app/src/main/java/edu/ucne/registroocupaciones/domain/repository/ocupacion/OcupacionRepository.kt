package edu.ucne.registroocupaciones.domain.repository.ocupacion

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import kotlinx.coroutines.flow.Flow

interface OcupacionRepository {
    fun observeOcupaciones(): Flow<List<Ocupacion>>
    suspend fun getOcupacion(id: Int): Ocupacion?
    suspend fun upsert(ocupacion: Ocupacion): Int
    suspend fun delete(id: Int)
    suspend fun exists(id: Int): Boolean
}