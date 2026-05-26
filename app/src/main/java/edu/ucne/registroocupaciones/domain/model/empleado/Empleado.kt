package edu.ucne.registroocupaciones.domain.model.empleado

import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import java.time.LocalDate

data class Empleado(
    val empleadoId: Int = 0,
    val ocupacionId: Int = 0,
    val fechaIngreso: LocalDate = LocalDate.now(),
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: Double = 0.0,
    val frecuenciaPago: FrecuenciaPago = FrecuenciaPago.SEMANAL
)
