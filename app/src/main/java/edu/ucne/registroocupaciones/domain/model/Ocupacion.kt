package edu.ucne.registroocupaciones.domain.model

data class Ocupacion(
    val OcupacionId: Int = 0,
    val Descripcion: String = "",
    val Sueldo: Double = 0.0
)
