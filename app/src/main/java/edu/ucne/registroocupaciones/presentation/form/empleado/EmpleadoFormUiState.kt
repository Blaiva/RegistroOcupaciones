package edu.ucne.registroocupaciones.presentation.form.empleado

import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import java.time.LocalDate

data class EmpleadoFormUiState (
    val empleadoId: Int? = null,
    val ocupacionId: String = "",
    val descripcionOcupacion: String = "",
    val fechaIngreso: LocalDate = LocalDate.now(),
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: String = "",
    val frecuenciaPago: FrecuenciaPago = FrecuenciaPago.MENSUAL,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val ocupacionError: String? = null,
    val fechaIngresoError: String? = null,
    val nombresError: String? = null,
    val sexoError: String? = null,
    val sueldoError: String? = null,
    val frecuenciaPagoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)