package edu.ucne.registroocupaciones.presentation.list.empleado

sealed class EmpleadoListUiEvent {
    object Load: EmpleadoListUiEvent()
    object Refresh: EmpleadoListUiEvent()
    data class Delete(val id: Int): EmpleadoListUiEvent()
    data class ShowMessage(val message: String): EmpleadoListUiEvent()
    object ClearMessage: EmpleadoListUiEvent()
    object CreateNew: EmpleadoListUiEvent()
    data class Edit(val id: Int): EmpleadoListUiEvent()
}