package com.example.proyectotfg.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectotfg.ui.screens.PantallaPrincipal
import com.example.proyectotfg.ui.screens.PantallaLogin
import com.example.proyectotfg.viewmodel.HomeViewModel
import com.example.proyectotfg.viewmodel.LoginViewModel

/**
 * Sellado de rutas de la aplicación para una navegación tipada y segura.
 * 
 * @property ruta Cadena que define la ruta única de la pantalla en el NavHost.
 */
sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login")
    object Inicio : Pantalla("inicio")
}

/**
 * Componente principal de navegación que gestiona el flujo entre pantallas.
 * Configura el NavHost y define las transiciones basadas en el estado de autenticación.
 */
@Composable
fun NavegacionApp() {
    // Controlador de navegación que mantiene la pila de pantallas
    val controladorNavegacion = rememberNavController()
    
    // El ViewModel de inicio se declara aquí para compartir datos de la sesión entre pantallas
    val viewModelInicio: HomeViewModel = viewModel()

    NavHost(
        navController = controladorNavegacion, 
        startDestination = Pantalla.Login.ruta
    ) {
        // Definición de la pantalla de inicio de sesión
        composable(Pantalla.Login.ruta) {
            val viewModelLogin: LoginViewModel = viewModel()
            PantallaLogin(
                viewModel = viewModelLogin,
                alIniciarSesionExitosamente = { respuesta ->
                    // Cargamos los datos recibidos en el ViewModel principal
                    viewModelInicio.setInitialData(respuesta)
                    // Navegamos a la pantalla de inicio eliminando el login de la pila
                    controladorNavegacion.navigate(Pantalla.Inicio.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                }
            )
        }
        
        // Definición de la pantalla principal de estadísticas
        composable(Pantalla.Inicio.ruta) {
            PantallaPrincipal(
                viewModel = viewModelInicio,
                alCerrarSesion = {
                    // Navegamos de vuelta al Login limpiando el historial
                    controladorNavegacion.navigate(Pantalla.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}