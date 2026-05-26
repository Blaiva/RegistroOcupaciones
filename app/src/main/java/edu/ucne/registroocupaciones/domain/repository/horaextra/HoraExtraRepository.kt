package edu.ucne.registroocupaciones.domain.repository.horaextra

import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import kotlinx.coroutines.flow.Flow

interface HoraExtraRepository {
    fun observeHorasExtra(): Flow<List<HoraExtra>>
    suspend fun getHoraExtra(id: Int): HoraExtra?
    suspend fun upsert(horaExtra: HoraExtra): Int
    suspend fun delete(id: Int)
    suspend fun exists(id: Int): Boolean
}