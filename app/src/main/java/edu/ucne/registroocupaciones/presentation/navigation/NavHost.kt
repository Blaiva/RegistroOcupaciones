package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormScreen
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiEvent
import edu.ucne.registroocupaciones.presentation.form.ocupacion.OcupacionFormScreen
import edu.ucne.registroocupaciones.presentation.list.empleado.EmpleadoListScreen
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListScreen

@Composable
fun RegistroNavHost(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues
){
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList,
        modifier = Modifier.padding(innerPadding)
    ){
        composable<Screen.OcupacionList> {
            OcupacionListScreen(
                onAddOcupacion = {
                    navController.navigate(Screen.OcupacionForm())
                },
                onEditOcupacion = { id ->
                    navController.navigate(Screen.OcupacionForm(ocupacionId = id))
                }
            )
        }
        composable<Screen.OcupacionForm> {
            OcupacionFormScreen (
                onBack = { navController.navigateUp() }
            )
        }

        composable<Screen.EmpleadoList> {
            EmpleadoListScreen (
                onAddEmpleado = {navController.navigate(Screen.EmpleadoForm())},
                onEditEmpleado = {id ->
                    navController.navigate(Screen.EmpleadoForm(empleadoId = id))
                }
            )
        }

        composable<Screen.EmpleadoForm> {
            EmpleadoFormScreen(
                onBack = {navController.navigateUp()}
            )
        }
    }
}