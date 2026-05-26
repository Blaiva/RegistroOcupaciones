package edu.ucne.registroocupaciones.presentation.list.horaextra

import android.os.Message
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra

data class HoraExtraListUiState(
    val horasExtra: List<HoraExtra> = emptyList(),
    val empleados: List<Empleado> = emptyList(),
    val isLoading: Boolean = false,
    val navigateToCreate: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val navigateToEdit: Int? = null
)