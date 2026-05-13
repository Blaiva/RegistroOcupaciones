package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.model.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int> {
        val listaActual = repository.observeOcupaciones().first().map { it.descripcion }
        val descriptionResult = validarDescripcion(ocupacion.descripcion, listaActual)
        if(!descriptionResult.isValid){
            return Result.failure(IllegalArgumentException(descriptionResult.error))
        }
        val sueldoResult = validarSueldo(ocupacion.sueldo.toString())
        if(!sueldoResult.isValid){
            return Result.failure(IllegalArgumentException(sueldoResult.error))
        }
        return runCatching { repository.upsert(ocupacion) }
    }
}