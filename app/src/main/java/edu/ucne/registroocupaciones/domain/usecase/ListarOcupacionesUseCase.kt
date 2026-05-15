package edu.ucne.registroocupaciones.domain.usecase

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarOcupacionesUseCase @Inject constructor(private val repository: OcupacionRepository) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}