package edu.ucne.registroocupaciones.domain.usecase.ocupacion

import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import javax.inject.Inject

class EliminarOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}