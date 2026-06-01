package edu.ucne.registroocupaciones.presentation.form.empleado

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoFormScreen(
    empleadoId: Int,
    viewModel: EmpleadoFormViewModel = hiltViewModel(),
    onBack: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()
    var sexoExpanded by remember { mutableStateOf(false) }
    var ocupacionExpanded by remember { mutableStateOf(false) }
    var frecuenciaPagoExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = empleadoId) {
        viewModel.loadEmpleado(empleadoId)
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted){
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Nuevo empleado" else "Editar empleado") },
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
                expanded = ocupacionExpanded,
                onExpandedChange = { ocupacionExpanded = !ocupacionExpanded }
            ) {
                OutlinedTextField(
                    value = state.descripcionOcupacion,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ocupación") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ocupacionExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("input_ocupacion"),
                    isError = state.ocupacionError != null,
                    supportingText = state.ocupacionError?.let { errorMsg -> { Text(errorMsg) } }
                )

                ExposedDropdownMenu(
                    expanded = ocupacionExpanded,
                    onDismissRequest = { ocupacionExpanded = false }
                ) {
                    if (state.ocupaciones.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Cargando ocupaciones...") },
                            onClick = { },
                            enabled = false
                        )
                    } else {
                        state.ocupaciones.forEach { ocupacion ->
                            DropdownMenuItem(
                                text = { Text(ocupacion.descripcion) },
                                onClick = {
                                    viewModel.onEvent(EmpleadoFormUiEvent.OcupacionChanged(ocupacion.ocupacionId.toString()))
                                    viewModel.onEvent(EmpleadoFormUiEvent.DescripcionOcupacionChanged(ocupacion.descripcion))
                                    ocupacionExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.fechaIngreso.toString(),
                onValueChange = { },
                readOnly = true,
                label = {Text("Fecha de ingreso")},
                trailingIcon = {
                    IconButton(onClick = {showDatePicker = true}) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable{showDatePicker = true}.testTag("input_fecha_ingreso"),
                isError = state.fechaIngresoError != null,
                supportingText = state.fechaIngresoError?.let { errorMsg -> {Text(errorMsg)} }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                viewModel.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(date))
                            }
                            showDatePicker = false
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = state.nombres,
                onValueChange = {viewModel.onEvent(EmpleadoFormUiEvent.NombresChanged(it))},
                label = {Text("Nombres")},
                modifier = Modifier.fillMaxWidth().testTag("input_nombres"),
                isError = state.nombresError != null,
                supportingText = state.nombresError?.let { errorMsg -> {Text(errorMsg)} },
                singleLine = true
            )

            val opcionesSexo = listOf("Masculino", "Femenino")
            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = {sexoExpanded = !sexoExpanded}
            ) {
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Sexo")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded)},
                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_sexo"),
                    isError = state.sexoError != null,
                    supportingText = state.sexoError?.let { errorMsg -> {Text(errorMsg)} }
                )
                ExposedDropdownMenu(
                    expanded = sexoExpanded,
                    onDismissRequest = {sexoExpanded = false}
                ) {
                    opcionesSexo.forEach { opcion ->
                        DropdownMenuItem(
                            text = {Text(opcion)},
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.SexoChanged(opcion))
                                sexoExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.sueldo,
                onValueChange = {viewModel.onEvent(EmpleadoFormUiEvent.SueldoChanged(it))},
                label = {Text("Sueldo")},
                modifier = Modifier.fillMaxWidth().testTag("input_sueldo"),
                isError = state.sueldoError != null,
                supportingText = state.sueldoError?.let { errorMsg -> {Text(errorMsg)} },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            val opcionesFrecuencia = FrecuenciaPago.entries
            ExposedDropdownMenuBox(
                expanded = frecuenciaPagoExpanded,
                onExpandedChange = { frecuenciaPagoExpanded = !frecuenciaPagoExpanded }
            ) {
                OutlinedTextField(
                    value = state.frecuenciaPago.descripcion,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frecuencia de Pago") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frecuenciaPagoExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("input_frecuencia_pago"),
                    isError = state.frecuenciaPagoError != null,
                    supportingText = state.frecuenciaPagoError?.let { errorMsg -> { Text(errorMsg) } }
                )
                ExposedDropdownMenu(
                    expanded = frecuenciaPagoExpanded,
                    onDismissRequest = { frecuenciaPagoExpanded = false }
                ) {
                    opcionesFrecuencia.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.descripcion) },
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.FrecuenciaPagoChanged(opcion))
                                frecuenciaPagoExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {viewModel.onEvent(EmpleadoFormUiEvent.Save)},
                modifier = Modifier.fillMaxWidth().testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if(state.isSaving){
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else{
                    Text("Guardar")
                }
            }

            if (!state.isNew) {
                Button(
                    onClick = { viewModel.onEvent(EmpleadoFormUiEvent.Delete) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_delete"),
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    if (state.isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Eliminar")
                    }
                }
            }

        }
    }
}