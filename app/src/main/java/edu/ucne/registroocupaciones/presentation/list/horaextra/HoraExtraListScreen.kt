package edu.ucne.registroocupaciones.presentation.list.horaextra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import androidx.compose.foundation.lazy.items

@Composable
fun HoraExtraListScreen(
    viewModel: HoraExtraListViewModel = hiltViewModel(),
    onAddHoraExtra: () -> Unit,
    onEditHoraExtra: (Int) -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    HoraExtraListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onAddClick = onAddHoraExtra,
        onEditClick = onEditHoraExtra
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraListBody(
    state: HoraExtraListUiState,
    onEvent: (HoraExtraListUiEvent) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
){
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(HoraExtraListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.testTag("extra_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar horas extra"
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()){
            if(state.isLoading){
                CircularProgressIndicator(Modifier.align(Alignment.Center).testTag("cargando"))
            } else{
                if(state.horasExtra.isEmpty()){
                    Text(
                        text = "No hay horas extra",
                        modifier = Modifier.align(Alignment.Center).testTag("Mensaje_Vacio"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.horasExtra,
                            key = {it.empleadoId}
                        ){ horasExtra ->
                            HoraExtraItem(
                                horaExtra = horasExtra,
                                onEdit = {onEditClick(horasExtra.empleadoId)},
                                state = state
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HoraExtraItem(
    horaExtra: HoraExtra,
    onEdit: () -> Unit,
    state: HoraExtraListUiState
){
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable{onEdit()}) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = (state.empleados.find { it.empleadoId == horaExtra.empleadoId })?.nombres ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = horaExtra.fecha.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = horaExtra.cantidadHoras.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = horaExtra.tipo.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "RD$${horaExtra.recargo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}