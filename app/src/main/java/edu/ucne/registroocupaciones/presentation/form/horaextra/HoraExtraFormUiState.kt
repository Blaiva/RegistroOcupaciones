package edu.ucne.registroocupaciones.presentation.form.horaextra

import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import java.time.LocalDate

data class HoraExtraFormUiState (
    val horaExtraId: Int? = null,
    val empleadoId: String = "",
    val nombreEmpleado: String = "",
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras: String = "",
    val tipo: TipoHoraExtra = TipoHoraExtra.DIURNO,
    val empleados: List<Empleado> = emptyList(),
    val empleadoError: String? = null,
    val fechaError: String? = null,
    val cantidadHorasError: String? = null,
    val tipoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)