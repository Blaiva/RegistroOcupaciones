package edu.ucne.registroocupaciones.presentation.form.ocupacion

sealed interface OcupacionFormUiEvent {
    data class Load(val id: Int): OcupacionFormUiEvent
    data class DescripcionChanged(val value: String): OcupacionFormUiEvent
    data class esPuestoDireccionChanged(val value: Boolean): OcupacionFormUiEvent
    data object Save: OcupacionFormUiEvent
    data object Delete: OcupacionFormUiEvent
}