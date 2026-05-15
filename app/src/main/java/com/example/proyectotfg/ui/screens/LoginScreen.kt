package com.example.proyectotfg.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import com.example.proyectotfg.R
import com.example.proyectotfg.model.LoginResponse
import com.example.proyectotfg.ui.theme.NeonBlue
import com.example.proyectotfg.ui.theme.NeonViolet
import com.example.proyectotfg.viewmodel.LoginViewModel

/**
 * Pantalla de inicio de sesión que permite al usuario autenticarse en el sistema.
 * Implementa desplazamiento vertical y adaptabilidad para evitar que el contenido se descuadre en horizontal.
 */
@Composable
fun PantallaLogin(
    viewModel: LoginViewModel,
    alIniciarSesionExitosamente: (LoginResponse) -> Unit
) {
    val nombreUsuario by viewModel.username.collectAsState()
    val contrasena by viewModel.password.collectAsState()
    val error by viewModel.error.collectAsState()
    val cargando by viewModel.isLoading.collectAsState()
    val respuestaLogin by viewModel.loginResponse.collectAsState()

    // Gestión de orientación y scroll
    val estadoDesplazamiento = rememberScrollState()
    val configuracion = LocalConfiguration.current
    val esHorizontal = configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(respuestaLogin) {
        respuestaLogin?.let {
            alIniciarSesionExitosamente(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Fondo adaptativo
        Image(
            painter = painterResource(id = R.drawable.fondo2_2),
            contentDescription = "Fondo de cielo estrellado",
            modifier = Modifier
                .fillMaxSize()
                .scale(if (esHorizontal) 1.2f else 1.8f),
            contentScale = if (esHorizontal) ContentScale.Crop else ContentScale.FillBounds
        )

        // Capa de contraste sutil
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        // Contenido con Scroll para evitar cortes
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(estadoDesplazamiento)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top 
        ) {
            // Espaciado dinámico: menos espacio arriba en horizontal
            Spacer(modifier = Modifier.height(if (esHorizontal) 20.dp else 110.dp)) 

            // Logo: Tamaño reducido en modo horizontal para que se ajuste
            Image(
                painter = painterResource(id = R.drawable.logo_rosa),
                contentDescription = "Logo Neon Defender",
                modifier = Modifier
                    .size(if (esHorizontal) 150.dp else 230.dp)
                    .padding(bottom = if (esHorizontal) 10.dp else 20.dp),
                contentScale = ContentScale.Fit
            )

            // Contenedor del Formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = { Text("Usuario", color = NeonBlue) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = NeonBlue.copy(alpha = 0.8f),
                        focusedLabelColor = NeonBlue,
                        cursorColor = NeonBlue,
                        unfocusedContainerColor = Color(0xFF121212),
                        focusedContainerColor = Color(0xFF121212)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Contraseña", color = NeonViolet) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonViolet,
                        unfocusedBorderColor = NeonViolet.copy(alpha = 0.8f),
                        focusedLabelColor = NeonViolet,
                        cursorColor = NeonViolet,
                        unfocusedContainerColor = Color(0xFF121212),
                        focusedContainerColor = Color(0xFF121212)
                    ),
                    singleLine = true
                )

                if (error != null) {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(if (esHorizontal) 10.dp else 20.dp))

                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(28.dp), ambientColor = NeonBlue, spotColor = NeonBlue),
                    enabled = !cargando,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = if (cargando) listOf(Color.Gray, Color.DarkGray) else listOf(NeonBlue, NeonViolet)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "INICIAR SESIÓN",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }
                }
                
                // Espacio extra al final para permitir scroll cómodo en horizontal
                Spacer(modifier = Modifier.height(if (esHorizontal) 40.dp else 20.dp))
            }
        }
    }
}