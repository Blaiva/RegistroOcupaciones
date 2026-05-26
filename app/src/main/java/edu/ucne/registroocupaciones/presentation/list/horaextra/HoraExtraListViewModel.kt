package edu.ucne.registroocupaciones.presentation.list.horaextra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.usecase.empleado.ListarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.ListarHoraExtraUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoraExtraListViewModel @Inject constructor(
    private val observeHoraExtraUseCase: ListarHoraExtraUseCase,
    private val observeEmpleadoUseCase: ListarEmpleadoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HoraExtraListUiState(isLoading = true))
    val state: StateFlow<HoraExtraListUiState> = _state.asStateFlow()

    init {
        loadHorasExtra()
        loadEmpleados()
    }

    fun onEvent(event: HoraExtraListUiEvent){
        return when(event){
            HoraExtraListUiEvent.Load -> loadHorasExtra()
            HoraExtraListUiEvent.Refresh -> loadHorasExtra()
            is HoraExtraListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            HoraExtraListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            HoraExtraListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is HoraExtraListUiEvent.Edit -> _state.update { it.copy(navigateToEdit = event.id) }
        }
    }

    fun loadHorasExtra(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeHoraExtraUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        horasExtra = list,
                        message = null
                    )
                }
            }
        }
    }

    fun loadEmpleados(){
        viewModelScope.launch {
            observeEmpleadoUseCase().collectLatest { list ->
                _state.update { it.copy(empleados = list) }
            }
        }
    }
}