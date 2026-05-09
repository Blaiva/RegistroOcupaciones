package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.repository.OcupacionRepository

class EliminarOcupacionUseCase(private val repository: OcupacionRepository) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}