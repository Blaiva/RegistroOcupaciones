package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarHoraExtraUseCase @Inject constructor(private val repository: HoraExtraRepository) {
    operator fun invoke(): Flow<List<HoraExtra>> = repository.observeHorasExtra()
}