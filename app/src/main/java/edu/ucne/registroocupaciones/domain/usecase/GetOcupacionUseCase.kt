package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository) {
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOcupacion(id)
}