package edu.ucne.registroocupaciones.domain.usecase.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(private val repository: EmpleadoRepository) {
    suspend operator fun invoke(empleado: Empleado): Result<Int> {
        val fechaResult = validarFecha(empleado.fechaIngreso)
        if(!fechaResult.isValid) {
            return Result.failure(IllegalArgumentException(fechaResult.error))
        }

        val nombresResult = validarNombres(empleado.nombres)
        if(!nombresResult.isValid) {
            return Result.failure(IllegalArgumentException(nombresResult.error))
        }

        val sexoResult = validarSexo(empleado.sexo)
        if(!sexoResult.isValid) {
            return Result.failure(IllegalArgumentException(sexoResult.error))
        }

        val sueldoResult = validarSueldo(empleado.sueldo.toString())
        if(!sueldoResult.isValid) {
            return Result.failure(IllegalArgumentException(sueldoResult.error))
        }

        return runCatching { repository.upsert(empleado) }
    }
}