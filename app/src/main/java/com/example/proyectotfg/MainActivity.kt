package com.example.proyectotfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyectotfg.navigation.NavegacionApp
import com.example.proyectotfg.ui.theme.ProyectoTFGTheme

/**
 * Actividad principal de la aplicación.
 * Punto de entrada que configura el tema visual y la navegación base.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Habilitar diseño de borde a borde para aprovechar toda la pantalla
        enableEdgeToEdge()
        
        setContent {
            // Aplicar el tema personalizado neón de la aplicación
            ProyectoTFGTheme {
                // Iniciar el flujo de navegación principal
                NavegacionApp()
            }
        }
    }
}