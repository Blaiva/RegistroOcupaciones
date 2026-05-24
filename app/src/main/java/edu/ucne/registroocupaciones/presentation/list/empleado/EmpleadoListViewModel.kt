package edu.ucne.registroocupaciones.presentation.list.empleado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.ListarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.ListarOcupacionesUseCase
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val listarEmpleadoUseCase: ListarEmpleadoUseCase,
    private val observeOcupacionesUseCase: ListarOcupacionesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(EmpleadoListUiState(isLoading = true))
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
        loadOcupaciones()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when (event) {
            EmpleadoListUiEvent.Load -> loadEmpleados()
            EmpleadoListUiEvent.Refresh -> loadEmpleados()
            is EmpleadoListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            EmpleadoListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            EmpleadoListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EmpleadoListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
        }
    }

    fun loadEmpleados() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            listarEmpleadoUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        empleados = list,
                        message = null
                    )
                }
            }
        }
    }

    fun loadOcupaciones() {
        viewModelScope.launch {
            observeOcupacionesUseCase().collectLatest { list -> _state.update { it.copy(ocupaciones = list) } }
        }
    }
}