package edu.ucne.registroocupaciones.presentation.form.ocupacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionFormScreen(
    ocupacionId: Int,
    viewModel: OcupacionFormViewModel = hiltViewModel(),
    onBack: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = ocupacionId) {
        viewModel.loadOcupacion(ocupacionId)
    }

    LaunchedEffect(state.saved, state.deleted) {
        if(state.saved || state.deleted)
        {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(state.isNew) "Nueva ocupacion" else "Editar ocupacion") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onEvent(OcupacionFormUiEvent.DescripcionChanged(it)) },
                label = {Text("Descripcion")},
                modifier = Modifier.fillMaxWidth().testTag("input_descripcion"),
                isError = state.descripcionError != null,
                supportingText = state.descripcionError?.let { errorMsg -> {Text(errorMsg)} },
                singleLine = false,
                minLines = 3,
                maxLines = 5
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = state.esPuestoDireccion,
                        onValueChange = { isChecked ->
                            viewModel.onEvent(OcupacionFormUiEvent.esPuestoDireccionChanged(isChecked))
                        },
                        role = androidx.compose.ui.semantics.Role.Checkbox
                    )
                    .padding(vertical = 8.dp)
                    .testTag("checkbox_puesto_direccion"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.esPuestoDireccion,
                    // Pasamos null aquí porque el clic ya lo maneja el 'toggleable' del Row
                    onCheckedChange = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "¿Es un puesto de dirección?",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = {viewModel.onEvent(OcupacionFormUiEvent.Save)},
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
                    onClick = { viewModel.onEvent(OcupacionFormUiEvent.Delete) },
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