package edu.ucne.registroocupaciones.presentation.list.ocupacion

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion

data class OcupacionListUiState(
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
