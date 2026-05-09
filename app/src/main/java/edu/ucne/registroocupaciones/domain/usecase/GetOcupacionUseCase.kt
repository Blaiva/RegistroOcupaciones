package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.model.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.OcupacionRepository

class GetOcupacionUseCase(private val repository: OcupacionRepository) {
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOcupacion(id)
}