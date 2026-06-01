package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registroocupaciones.presentation.adaptative.EmpleadoAdaptativeScreen
import edu.ucne.registroocupaciones.presentation.adaptative.HoraExtraAdaptativeScreen
import edu.ucne.registroocupaciones.presentation.adaptative.OcupacionAdaptativeScreen
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormScreen
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiEvent
import edu.ucne.registroocupaciones.presentation.form.horaextra.HoraExtraFormScreen
import edu.ucne.registroocupaciones.presentation.form.ocupacion.OcupacionFormScreen
import edu.ucne.registroocupaciones.presentation.list.empleado.EmpleadoListScreen
import edu.ucne.registroocupaciones.presentation.list.horaextra.HoraExtraListScreen
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListScreen

@Composable
fun RegistroNavHost(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues = PaddingValues()
){
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList,
        modifier = Modifier.padding(innerPadding)
    ){
        composable<Screen.EmpleadoList> {
            EmpleadoAdaptativeScreen()
        }
        composable<Screen.HoraExtraList> {
            HoraExtraAdaptativeScreen()
        }
        composable<Screen.OcupacionList> {
            OcupacionAdaptativeScreen()
        }
    }
}