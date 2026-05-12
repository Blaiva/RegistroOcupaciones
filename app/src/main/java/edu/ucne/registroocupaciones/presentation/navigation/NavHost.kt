package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registroocupaciones.presentation.form.OcupacionFormScreen
import edu.ucne.registroocupaciones.presentation.list.OcupacionListScreen

@Composable
fun TaskNavHost(
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList
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
    }
}