package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import javax.inject.Inject

class UpsertHoraExtraUseCase @Inject constructor(private val repository: HoraExtraRepository) {
    suspend operator fun invoke(horaExtra: HoraExtra): Result<Int>{
        val empleadoResult = validarEmpleado(horaExtra.empleadoId.toString())
        if(!empleadoResult.isValid){
            return Result.failure(IllegalArgumentException(empleadoResult.error))
        }

        val fechaResult = validarFecha(horaExtra.fecha)
        if(!fechaResult.isValid){
            return Result.failure(IllegalArgumentException(fechaResult.error))
        }

        val cantidadHorasResult = validarCantidadHoras(horaExtra.cantidadHoras.toString())
        if(!cantidadHorasResult.isValid){
            return Result.failure(IllegalArgumentException(fechaResult.error))
        }

        val tipoHoraExtraResult = validarTipoHoraExtra(horaExtra.tipo.descripcion, horaExtra.cantidadHoras)
        if(!tipoHoraExtraResult.isValid){
            return Result.failure(IllegalArgumentException(tipoHoraExtraResult.error))
        }

        return  runCatching { repository.upsert(horaExtra) }
    }
}