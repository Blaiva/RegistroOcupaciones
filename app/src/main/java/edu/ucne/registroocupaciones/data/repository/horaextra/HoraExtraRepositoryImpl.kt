package edu.ucne.registroocupaciones.data.repository.horaextra

import androidx.compose.foundation.interaction.InteractionSource
import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraDao
import edu.ucne.registroocupaciones.data.mapper.horaextra.toDomain
import edu.ucne.registroocupaciones.data.mapper.horaextra.toEntity
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HoraExtraRepositoryImpl @Inject constructor(private val localDataSource: HoraExtraDao): HoraExtraRepository {
    override fun observeHorasExtra(): Flow<List<HoraExtra>> {
        return localDataSource.listar().map { entities -> entities.map{ it.toDomain() } }
    }

    override suspend fun getHoraExtra(id: Int): HoraExtra? {
        return localDataSource.buscar(id)?.toDomain()
    }

    override suspend fun upsert(horaExtra: HoraExtra): Int {
        localDataSource.upsert(horaExtra.toEntity())
        return horaExtra.horaExtraId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.eliminar(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return localDataSource.existe(id)
    }
}