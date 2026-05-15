package edu.ucne.registroocupaciones.data.repository.empleado

import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoDao
import edu.ucne.registroocupaciones.data.mapper.empleado.toDomain
import edu.ucne.registroocupaciones.data.mapper.empleado.toEntity
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(private val localDataSource: EmpleadoDao): EmpleadoRepository{
    override fun observeEmpleados(): Flow<List<Empleado>> {
        return localDataSource.listar().map { entities -> entities.map{ it.toDomain() } }
    }

    override suspend fun getEmpleado(id: Int): Empleado? {
        return localDataSource.buscar(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int {
        localDataSource.upsert(empleado.toEntity())
        return empleado.empleadoId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.eliminar(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return localDataSource.existe(id)
    }
}