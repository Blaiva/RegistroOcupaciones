package edu.ucne.registroocupaciones.presentation.form.horaextra

import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra
import java.time.LocalDate

sealed interface HoraExtraFormUiEvent {
    data class Load(val id: Int): HoraExtraFormUiEvent
    data class EmpleadoChanged(val value: String): HoraExtraFormUiEvent
    data class NombreEmpleadoChanged(val value: String): HoraExtraFormUiEvent
    data class FechaChanged(val value: LocalDate): HoraExtraFormUiEvent
    data class CantidadHorasChanged(val value: String): HoraExtraFormUiEvent
    data class TipoChanged(val value: TipoHoraExtra): HoraExtraFormUiEvent
    data object Save: HoraExtraFormUiEvent
    data object Delete: HoraExtraFormUiEvent
}