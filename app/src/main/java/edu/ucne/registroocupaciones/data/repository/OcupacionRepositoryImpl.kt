package edu.ucne.registroocupaciones.data.repository

import edu.ucne.registroocupaciones.data.local.OcupacionDao
import edu.ucne.registroocupaciones.data.mapper.toDomain
import edu.ucne.registroocupaciones.data.mapper.toEntity
import edu.ucne.registroocupaciones.domain.model.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(
    private val localDataSource: OcupacionDao
): OcupacionRepository {
    override fun observeOcupaciones(): Flow<List<Ocupacion>> {
        return localDataSource.listar().map {
            entities -> entities.map { it.toDomain() }
        }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return localDataSource.buscar(id)?.toDomain();
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        localDataSource.upsert(ocupacion.toEntity())
        return ocupacion.ocupacionId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.eliminar(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return localDataSource.existe(id)
    }
}