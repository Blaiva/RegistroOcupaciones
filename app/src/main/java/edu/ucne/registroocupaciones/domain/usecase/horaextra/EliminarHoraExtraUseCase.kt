package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import javax.inject.Inject

class EliminarHoraExtraUseCase @Inject constructor(private val repository: HoraExtraRepository) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}