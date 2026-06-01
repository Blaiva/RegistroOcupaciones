package edu.ucne.registroocupaciones.presentation.form.horaextra

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import edu.ucne.registroocupaciones.domain.repository.horaextra.HoraExtraRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.ListarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.EliminarHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.GetHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.ListarHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.UpsertHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.usecase.horaextra.calcularRecargoHoraExtra
import edu.ucne.registroocupaciones.domain.usecase.horaextra.validarCantidadHoras
import edu.ucne.registroocupaciones.domain.usecase.horaextra.validarEmpleado
import edu.ucne.registroocupaciones.domain.usecase.horaextra.validarFecha
import edu.ucne.registroocupaciones.domain.usecase.horaextra.validarTipoHoraExtra
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.ListarOcupacionesUseCase
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiEvent
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiState
import edu.ucne.registroocupaciones.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoraExtraFormViewModel @Inject constructor(
    private val repository: HoraExtraRepository,
    private val getHoraExtraUseCase: GetHoraExtraUseCase,
    private val upsertHoraExtraUseCase: UpsertHoraExtraUseCase,
    private val eliminarHoraExtraUseCase: EliminarHoraExtraUseCase,
    private val observeEmpleadoUseCase: ListarEmpleadoUseCase,
    private val observeOcupacionUseCase: ListarOcupacionesUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val routeArgs = savedStateHandle.toRoute<Screen.HoraExtraForm>()
    private val horaExtraId: Int = routeArgs.horaExtraId

    private val _state = MutableStateFlow(HoraExtraFormUiState())
    val state: StateFlow<HoraExtraFormUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
    }

    fun onEvent(event: HoraExtraFormUiEvent){
        when(event){
            is HoraExtraFormUiEvent.Load -> loadHoraExtra(event.id)
            is HoraExtraFormUiEvent.EmpleadoChanged -> _state.update { it.copy(empleadoId = event.value, empleadoError = null) }
            is HoraExtraFormUiEvent.NombreEmpleadoChanged -> _state.update { it.copy(nombreEmpleado = event.value) }
            is HoraExtraFormUiEvent.FechaChanged -> _state.update { it.copy(fecha = event.value, fechaError = null) }
            is HoraExtraFormUiEvent.CantidadHorasChanged -> _state.update { it.copy(cantidadHoras = event.value, cantidadHorasError = null) }
            is HoraExtraFormUiEvent.TipoChanged -> _state.update { it.copy(tipo = event.value, tipoError = null) }
            HoraExtraFormUiEvent.Save -> onSave()
            HoraExtraFormUiEvent.Delete -> onDelete()
        }
    }

    fun loadEmpleados(){
        viewModelScope.launch {
            observeEmpleadoUseCase().collectLatest { list -> _state.update { it.copy(empleados = list) } }
        }
    }

    fun loadHoraExtra(id: Int) {
        if(id == 0 ){
            val empleados = _state.value.empleados
            _state.value = HoraExtraFormUiState(
                empleados = empleados
            )
            return
        }

        viewModelScope.launch {
            val horaExtra = getHoraExtraUseCase(id)
            val empleados = observeEmpleadoUseCase().first()

            if(horaExtra != null){
                _state.update {
                    it.copy(
                        isNew = false,
                        horaExtraId = horaExtra.horaExtraId,
                        empleadoId = horaExtra.empleadoId.toString(),
                        nombreEmpleado = (empleados.find { it.empleadoId == horaExtra.empleadoId })?.nombres ?: "",
                        fecha = horaExtra.fecha,
                        cantidadHoras = horaExtra.cantidadHoras.toString(),
                        tipo = horaExtra.tipo
                    )
                }
            }else{
                _state.update { it.copy(isNew = true, horaExtraId = null) }
            }
        }
    }

    private fun onSave(){
        val empleadoId = state.value.empleadoId
        val fecha = state.value.fecha
        val cantidadHoras = state.value.cantidadHoras
        val tipo = state.value.tipo

        val empleadoValidation = validarEmpleado(empleadoId)
        val fechaValidation = validarFecha(fecha)
        val cantidadHorasValidation = validarCantidadHoras(cantidadHoras)

        if(!empleadoValidation.isValid || !fechaValidation.isValid || !cantidadHorasValidation.isValid){
            _state.update {
                it.copy(
                    empleadoError = empleadoValidation.error,
                    fechaError = fechaValidation.error,
                    cantidadHorasError = cantidadHorasValidation.error
                )
            }
            return
        }

        val tipoValidation = validarTipoHoraExtra(tipo.descripcion, cantidadHoras.toInt())

        if(!tipoValidation.isValid){
            _state.update {
                it.copy(
                    tipoError = tipoValidation.error
                )
            }
            return
        }

        val empleado = state.value.empleados.find { it.empleadoId ==  empleadoId.toInt()}

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            if(empleado != null) {
                val esPuestoDireccion = (observeOcupacionUseCase().first().find { it.ocupacionId == empleado.ocupacionId }?.esPuestoDireccion ?: false)
                val recargo = calcularRecargoHoraExtra(
                    sueldo = empleado.sueldo,
                    frecuenciaPago = empleado.frecuenciaPago,
                    tipoHoraExtra = tipo,
                    cantidadHoras = cantidadHoras.toInt(),
                    esPuestoDireccion = esPuestoDireccion
                )


                val horaExtra = HoraExtra(
                    horaExtraId = state.value.horaExtraId ?: 0,
                    empleadoId = empleadoId.toInt(),
                    fecha = fecha,
                    cantidadHoras = cantidadHoras.toInt(),
                    tipo = tipo,
                    recargo = recargo
                )


                val result = upsertHoraExtraUseCase(horaExtra)

                result.onSuccess { newId ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saved = true,
                            horaExtraId = newId,
                            isNew = false
                        )
                    }
                }.onFailure { _state.update { it.copy(isSaving = false) } }
            }
        }
    }

    private fun onDelete(){
        val id = state.value.horaExtraId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            eliminarHoraExtraUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}