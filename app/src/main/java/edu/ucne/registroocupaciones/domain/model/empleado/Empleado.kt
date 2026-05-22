package edu.ucne.registroocupaciones.domain.model.empleado

import java.time.LocalDate

data class Empleado(
    val empleadoId: Int = 0,
    val fechaIngreso: LocalDate = LocalDate.now(),
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: Double = 0.0
)
