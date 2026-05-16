package edu.ucne.registroocupaciones.domain.usecase.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import javax.inject.Inject


class GetEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository) {
    suspend operator fun invoke(id: Int): Empleado? = repository.getEmpleado(id)
}