package edu.ucne.registroocupaciones.domain.model.horaextra

import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra
import java.time.LocalDate

data class HoraExtra(
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras: Int = 0,
    val tipo: TipoHoraExtra = TipoHoraExtra.DIURNO,
    val recargo: Double = 0.0
)
