package edu.ucne.registroocupaciones.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object OcupacionList: Screen()
    @Serializable
    data class OcupacionForm(val ocupacionId: Int = 0): Screen()
    @Serializable
    data object EmpleadoList: Screen()
    @Serializable
    data class EmpleadoForm(val empleadoId: Int = 0): Screen()
}