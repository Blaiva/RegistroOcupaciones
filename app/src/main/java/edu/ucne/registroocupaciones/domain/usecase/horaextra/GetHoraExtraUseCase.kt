package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import javax.inject.Inject

class GetHoraExtraUseCase @Inject constructor(private val repository: HoraExtraRepository) {
    suspend operator fun invoke(id: Int): HoraExtra? = repository.getHoraExtra(id)
}