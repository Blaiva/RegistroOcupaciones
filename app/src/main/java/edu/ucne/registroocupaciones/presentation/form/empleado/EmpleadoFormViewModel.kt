package edu.ucne.registroocupaciones.presentation.form.empleado

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarFecha
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarFrecuenciaPago
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarNombres
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarOcupacion
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarSexo
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarSueldo
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.GetOcupacionUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.ListarOcupacionesUseCase
import edu.ucne.registroocupaciones.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val repository: EmpleadoRepository,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val eliminarEmpleadoUseCase: EliminarEmpleadoUseCase,
    private val observeOcupacionesUseCase: ListarOcupacionesUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoForm>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    init {
        loadEmpleado(empleadoId)
        loadOcupaciones()
    }

    fun onEvent(event: EmpleadoFormUiEvent){
        when(event){
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.OcupacionChanged -> _state.update { it.copy(ocupacionId = event.value, ocupacionError = null) }
            is EmpleadoFormUiEvent.DescripcionOcupacionChanged -> _state.update { it.copy(descripcionOcupacion = event.value) }
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
            is EmpleadoFormUiEvent.NombresChanged -> _state.update { it.copy(nombres = event.value, nombresError = null) }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update { it.copy(sexo = event.value, sexoError = null) }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update { it.copy(sueldo = event.value, sueldoError = null) }
            is EmpleadoFormUiEvent.FrecuenciaPagoChanged -> _state.update { it.copy(frecuenciaPago = event.value, sueldoError = null) }
            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
        }
    }

    fun loadOcupaciones()
    {
        viewModelScope.launch {
            observeOcupacionesUseCase().collectLatest { list -> _state.update { it.copy(ocupaciones = list)} }
        }
    }

    private fun loadEmpleado(id: Int?){
        if(id == null || id == 0){
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }

        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            if(empleado != null){
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = empleado.empleadoId,
                        ocupacionId = empleado.ocupacionId.toString(),
                        fechaIngreso = empleado.fechaIngreso,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        sueldo = empleado.sueldo.toString(),
                        frecuenciaPago = empleado.frecuenciaPago
                    )
                }
            }else{
                _state.update { it.copy(isNew = true, empleadoId = null) }
            }
        }
    }

    private fun onSave(){
        val ocupacionId = state.value.ocupacionId
        val fechaIngreso = state.value.fechaIngreso
        val nombres = state.value.nombres
        val sexo = state.value.sexo
        val sueldoText = state.value.sueldo
        val frecuenciaPago = state.value.frecuenciaPago

        val ocupacionValidation = validarOcupacion(ocupacionId)
        val fechaIngresoValidation = validarFecha(fechaIngreso)
        val nombresValidation = validarNombres(nombres)
        val sexoValidation = validarSexo(sexo)
        val sueldoValidation = validarSueldo(sueldoText)
        val frecuenciaPagoValidation = validarFrecuenciaPago(frecuenciaPago.descripcion)

        if(!ocupacionValidation.isValid || !fechaIngresoValidation.isValid || !nombresValidation.isValid || !sexoValidation.isValid || !sueldoValidation.isValid || !frecuenciaPagoValidation.isValid){
            _state.update {
                it.copy(
                    ocupacionError = ocupacionValidation.error,
                    fechaIngresoError = fechaIngresoValidation.error,
                    nombresError = nombresValidation.error,
                    sexoError = sexoValidation.error,
                    sueldoError = sueldoValidation.error,
                    frecuenciaPagoError = frecuenciaPagoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val empleado = Empleado(
                empleadoId = state.value.empleadoId ?: 0,
                ocupacionId = ocupacionId.toInt(),
                fechaIngreso = fechaIngreso,
                nombres = nombres,
                sexo = sexo,
                sueldo = sueldoText.toDouble(),
                frecuenciaPago = frecuenciaPago
            )

            val result = upsertEmpleadoUseCase(empleado)

            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        empleadoId = newId,
                        isNew = false
                    )
                }
            }.onFailure { _state.update { it.copy(isSaving = false) } }
        }
    }

    private fun onDelete(){
        val id = state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            eliminarEmpleadoUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }

}