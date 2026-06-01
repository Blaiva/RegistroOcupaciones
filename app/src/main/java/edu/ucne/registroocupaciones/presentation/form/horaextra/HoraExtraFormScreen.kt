package edu.ucne.registroocupaciones.presentation.form.horaextra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraFormScreen(
    horaExtraId: Int,
    viewModel: HoraExtraFormViewModel = hiltViewModel(),
    onBack: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var empleadoExpanded by remember { mutableStateOf(false) }
    var tipoExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = horaExtraId) {
        viewModel.loadHoraExtra(horaExtraId)
    }

    LaunchedEffect(state.saved, state.deleted) {
        if(state.saved || state.deleted){
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(state.isNew) "Nuevas horas extra" else "Editar horas extras") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = empleadoExpanded,
                onExpandedChange = {empleadoExpanded = !empleadoExpanded}
            ) {
                OutlinedTextField(
                    value = state.nombreEmpleado,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Empleado")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = empleadoExpanded)},
                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_empleado"),
                    isError = state.empleadoError != null,
                    supportingText = state.empleadoError?.let { errorMsg -> {Text(errorMsg)} }
                )

                ExposedDropdownMenu(
                    expanded = empleadoExpanded,
                    onDismissRequest = {empleadoExpanded = false}
                ) {
                    if(state.empleados.isEmpty()){
                        DropdownMenuItem(
                            text = {Text("Cargando empleados...")},
                            onClick = {},
                            enabled = false
                        )
                    }else{
                        state.empleados.forEach { empleado ->
                            DropdownMenuItem(
                                text = {Text(empleado.nombres)},
                                onClick = {
                                    viewModel.onEvent(HoraExtraFormUiEvent.EmpleadoChanged(empleado.empleadoId.toString()))
                                    viewModel.onEvent(HoraExtraFormUiEvent.NombreEmpleadoChanged(empleado.nombres))
                                    empleadoExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.fecha.toString(),
                onValueChange = {},
                readOnly = true,
                label = {Text("Fecha")},
                trailingIcon = {
                    IconButton(onClick = {showDatePicker = true}) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable{showDatePicker = true}.testTag("input_fecha"),
                isError = state.fechaError != null,
                supportingText = state.fechaError?.let { errorMsg -> {Text(errorMsg)} }
            )

            if(showDatePicker){
                DatePickerDialog(
                    onDismissRequest = {showDatePicker = false},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val date = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDate()
                                    viewModel.onEvent(HoraExtraFormUiEvent.FechaChanged(date))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {showDatePicker = false}) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = state.cantidadHoras,
                onValueChange = {viewModel.onEvent(HoraExtraFormUiEvent.CantidadHorasChanged(it))},
                label = {Text("Cantidad de Horas Extra")},
                modifier = Modifier.fillMaxWidth().testTag("input_horas"),
                isError = state.cantidadHorasError != null,
                supportingText = state.cantidadHorasError?.let{errorMsg -> {Text(errorMsg)}},
                singleLine = true
            )

            val opcionesTipo = TipoHoraExtra.entries
            ExposedDropdownMenuBox(
                expanded = tipoExpanded,
                onExpandedChange = {tipoExpanded = !tipoExpanded}
            ) {
                OutlinedTextField(
                    value = state.tipo.descripcion,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Tipo de Hora Extra")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded)},
                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("tipo_input"),
                    isError = state.tipoError != null,
                    supportingText = state.tipoError?.let { errorMsg -> {Text(errorMsg)} }
                )
                ExposedDropdownMenu(
                    expanded = tipoExpanded,
                    onDismissRequest = {tipoExpanded = false}
                ) {
                    opcionesTipo.forEach { opcion ->
                        DropdownMenuItem(
                            text = {Text(opcion.descripcion)},
                            onClick = {
                                viewModel.onEvent(HoraExtraFormUiEvent.TipoChanged(opcion))
                                tipoExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {viewModel.onEvent(HoraExtraFormUiEvent.Save)},
                modifier = Modifier.fillMaxWidth().testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if (state.isSaving){
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }else{
                    Text("Guardar")
                }
            }

            if(!state.isNew){
                Button(
                    onClick = {viewModel.onEvent(HoraExtraFormUiEvent.Delete)},
                    modifier = Modifier.fillMaxWidth().testTag("btn_delete"),
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    if(state.isDeleting){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    }else {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}