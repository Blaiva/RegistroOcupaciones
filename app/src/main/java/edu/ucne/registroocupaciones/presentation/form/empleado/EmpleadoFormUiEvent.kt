package edu.ucne.registroocupaciones.presentation.form.empleado

import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import edu.ucne.registroocupaciones.presentation.form.ocupacion.OcupacionFormUiEvent
import java.time.LocalDate

sealed interface EmpleadoFormUiEvent {
    data class Load(val id: Int): EmpleadoFormUiEvent
    data class FechaIngresoChanged(val value: LocalDate): EmpleadoFormUiEvent
    data class NombresChanged(val value: String): EmpleadoFormUiEvent
    data class SexoChanged(val value: String): EmpleadoFormUiEvent
    data class SueldoChanged(val value: String): EmpleadoFormUiEvent
    data class OcupacionChanged(val value: String): EmpleadoFormUiEvent
    data class DescripcionOcupacionChanged(val value: String): EmpleadoFormUiEvent
    data class FrecuenciaPagoChanged(val value: FrecuenciaPago): EmpleadoFormUiEvent
    data object Save: EmpleadoFormUiEvent
    data object Delete: EmpleadoFormUiEvent
}