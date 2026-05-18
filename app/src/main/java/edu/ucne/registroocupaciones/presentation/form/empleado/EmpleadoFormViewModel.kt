package edu.ucne.registroocupaciones.presentation.form.empleado

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarFecha
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarNombres
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarSexo
import edu.ucne.registroocupaciones.domain.usecase.empleado.validarSueldo
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListUiEvent
import edu.ucne.registroocupaciones.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val repository: EmpleadoRepository,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val eliminarEmpleadoUseCase: EliminarEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoForm>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    init {
        loadEmpleado(empleadoId)
    }

    fun onEvent(event: EmpleadoFormUiEvent){
        when(event){
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
            is EmpleadoFormUiEvent.NombresChanged -> _state.update { it.copy(nombres = event.value, nombresError = null) }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update { it.copy(sexo = event.value, sexoError = null) }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update { it.copy(sueldo = event.value, sueldoError = null) }
            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
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
                        fechaIngreso = empleado.fechaIngreso,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        sueldo = empleado.sueldo.toString()
                    )
                }
            }else{
                _state.update { it.copy(isNew = true, empleadoId = null) }
            }
        }
    }

    private fun onSave(){
        val fechaIngreso = state.value.fechaIngreso
        val nombres = state.value.nombres
        val sexo = state.value.sexo
        val sueldoText = state.value.sueldo

        val fechaIngresoValidation = validarFecha(fechaIngreso)
        val nombresValidation = validarNombres(nombres)
        val sexoValidation = validarSexo(sexo)
        val sueldoValidation = validarSueldo(sueldoText)

        if(!fechaIngresoValidation.isValid || !nombresValidation.isValid || !sexoValidation.isValid || !sueldoValidation.isValid){
            _state.update {
                it.copy(
                    fechaIngresoError = fechaIngresoValidation.error,
                    nombresError = nombresValidation.error,
                    sexoError = sexoValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val empleado = Empleado(
                empleadoId = state.value.empleadoId ?: 0,
                fechaIngreso = fechaIngreso,
                nombres = nombres,
                sexo = sexo,
                sueldo = sueldoText.toDouble()
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