package edu.ucne.registroocupaciones.domain.usecase.ocupacion

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import kotlinx.coroutines.flow.first
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

        return runCatching { repository.upsert(ocupacion) }
    }
}