package edu.ucne.registroocupaciones.presentation.list.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
