package edu.ucne.registroocupaciones.domain.model.ocupacion

data class Ocupacion(
    val ocupacionId: Int = 0,
    val descripcion: String = "",
    val esPuestoDireccion: Boolean = false
)