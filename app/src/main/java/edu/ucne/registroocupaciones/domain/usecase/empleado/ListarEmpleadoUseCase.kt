package edu.ucne.registroocupaciones.domain.usecase.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository) {
    operator fun invoke(): Flow<List<Empleado>> = repository.observeEmpleados()
}