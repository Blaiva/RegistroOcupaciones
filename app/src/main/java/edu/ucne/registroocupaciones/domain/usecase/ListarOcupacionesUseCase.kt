package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.model.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow

class ListarOcupacionesUseCase(private val repository: OcupacionRepository) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}