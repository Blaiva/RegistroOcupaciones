package edu.ucne.registroocupaciones.domain.usecase.empleado

import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import javax.inject.Inject

class EliminarEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}