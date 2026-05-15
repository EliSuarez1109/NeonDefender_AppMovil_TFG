package com.example.proyectotfg.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotfg.R
import com.example.proyectotfg.model.*
import com.example.proyectotfg.ui.theme.*
import com.example.proyectotfg.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal de la aplicación que muestra las estadísticas del jugador y el historial de partidas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    viewModel: HomeViewModel,
    alCerrarSesion: () -> Unit
) {
    val estadoMenuLateral = rememberDrawerState(initialValue = DrawerValue.Closed)
    val ambitoCorrutina = rememberCoroutineScope()
    
    val listaPartidas by viewModel.games.collectAsState()
    val partidaSeleccionada by viewModel.selectedGame.collectAsState()
    val apodoUsuario by viewModel.nickname.collectAsState()

    ModalNavigationDrawer(
        drawerState = estadoMenuLateral,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(320.dp)
            ) {
                // Cabecera del menú lateral con información del usuario
                CabeceraMenuLateral(apodoUsuario)
                
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp), 
                    color = NeonBlue.copy(alpha = 0.3f)
                )
                
                Text(
                    "Últimas 20 Partidas",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonBlue
                )
                
                // Lista de partidas en el menú lateral con estados
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(listaPartidas) { partida ->
                            val colorIcono = when (partida.status) {
                                GameStatus.VICTORIA -> NeonBlue
                                GameStatus.DERROTA -> NeonViolet
                                GameStatus.RENDICION -> TextGray
                                GameStatus.INFINITO -> NeonYellow
                            }
                            
                            NavigationDrawerItem(
                                label = { 
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        EstadoTextoNeon(partida.status)
                                        Text(
                                            partida.date.split(" ")[0], 
                                            style = MaterialTheme.typography.bodySmall, 
                                            color = Color.Gray
                                        )
                                    }
                                },
                                selected = partidaSeleccionada?.id == partida.id,
                                onClick = { 
                                    viewModel.selectGame(partida)
                                    ambitoCorrutina.launch { estadoMenuLateral.close() } 
                                },
                                icon = { 
                                    Icon(
                                        Icons.Default.Star, 
                                        contentDescription = null, 
                                        tint = colorIcono
                                    ) 
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }

                // Sección inferior para cerrar sesión
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = NeonBlue.copy(alpha = 0.1f))
                NavigationDrawerItem(
                    label = { Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold) },
                    selected = false,
                    onClick = {
                        ambitoCorrutina.launch {
                            estadoMenuLateral.close()
                            alCerrarSesion()
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = NeonRed) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = NeonRed, 
                        unselectedIconColor = NeonRed
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ESTADÍSTICAS", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { ambitoCorrutina.launch { estadoMenuLateral.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = NeonBlue)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { rellenoPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(rellenoPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Detalles de la partida seleccionada
                item {
                    partidaSeleccionada?.let { partida ->
                        AnimatedContent(
                            targetState = partida,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "animacion_detalles"
                        ) { partidaObjetivo ->
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                CabeceraDetalle(partidaObjetivo)
                                TarjetaFechaHora(partidaObjetivo.date)
                                TarjetaDificultad(partidaObjetivo.difficulty.label.uppercase())
                                TarjetaRonda(partidaObjetivo.stats.rounds.toString())

                                Text(
                                    "Resultados de la Partida",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = NeonBlue
                                )
                                
                                TarjetaEstadistica(
                                    etiqueta = "Daño Total Infligido", 
                                    valor = "${partidaObjetivo.stats.damageDealt}", 
                                    color = NeonBlue,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                TarjetaEstadistica(
                                    etiqueta = "Daño Total Recibido", 
                                    valor = "${partidaObjetivo.stats.damageReceived}", 
                                    color = NeonViolet, 
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(), 
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TarjetaEstadistica(
                                        etiqueta = "Oro Ganado", 
                                        valor = "${partidaObjetivo.stats.goldEarned}", 
                                        color = NeonBlue, 
                                        modifier = Modifier.weight(1f)
                                    )
                                    TarjetaEstadistica(
                                        etiqueta = "Oro Gastado", 
                                        valor = "${partidaObjetivo.stats.goldSpent}", 
                                        color = NeonViolet, 
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                TablaTorres(partidaObjetivo.towersUsed)

                                TablaEnemigos(partidaObjetivo.enemiesKilled)
                                
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente que muestra el estado de la partida con sus respectivos colores.
 */
@Composable
fun EstadoTextoNeon(status: GameStatus, large: Boolean = false) {
    val style = if (large) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.bodyLarge
    
    when (status) {
        GameStatus.VICTORIA -> {
            Text(
                text = status.label.uppercase(),
                color = NeonBlue,
                fontWeight = FontWeight.ExtraBold,
                style = style
            )
        }
        GameStatus.DERROTA -> {
            Text(
                text = status.label.uppercase(),
                color = NeonViolet,
                fontWeight = FontWeight.ExtraBold,
                style = style
            )
        }
        GameStatus.RENDICION -> {
            Text(
                text = status.label.uppercase(),
                color = TextGray,
                fontWeight = FontWeight.ExtraBold,
                style = style
            )
        }
        GameStatus.INFINITO -> {
            Text(
                text = status.label.uppercase(),
                color = NeonYellow,
                fontWeight = FontWeight.ExtraBold,
                style = style
            )
        }
    }
}

@Composable
fun CabeceraDetalle(partida: Game) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.padding(24.dp).fillMaxWidth(), 
            contentAlignment = Alignment.Center
        ) {
            EstadoTextoNeon(partida.status, large = true)
        }
    }
}

@Composable
fun TarjetaFechaHora(fecha: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp), 
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "FECHA Y HORA: ", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = fecha, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = NeonBlue)
        }
    }
}

@Composable
fun TarjetaDificultad(dificultad: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp), 
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "DIFICULTAD: ", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = dificultad, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = NeonViolet)
        }
    }
}

@Composable
fun TarjetaRonda(ronda: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "RONDA: ", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = ronda, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = NeonViolet)
        }
    }
}

@Composable
fun TarjetaEstadistica(etiqueta: String, valor: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), 
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(etiqueta, style = MaterialTheme.typography.labelSmall)
            Text(
                valor, 
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, color = color)
            )
        }
    }
}

@Composable
fun TablaTorres(torres: List<TowerUsed>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), 
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // El título ocupa el espacio de la primera y segunda columna
                Text("Torres Utilizadas", modifier = Modifier.weight(3f), fontWeight = FontWeight.Bold, color = NeonBlue)
                Text("Cantidad", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = NeonBlue)
            }
            Divider(color = NeonBlue.copy(alpha = 0.2f))
            torres.forEach { torre ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), 
                    verticalAlignment = Alignment.CenterVertically, 
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = obtenerIdImagenTorre(torre.name)),
                        contentDescription = torre.name,
                        modifier = Modifier.size(60.dp).weight(1f),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "Torre ${torre.name}", 
                        modifier = Modifier.weight(2f), 
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "x${torre.count}", 
                        modifier = Modifier.weight(1f), 
                        fontWeight = FontWeight.Bold, 
                        color = NeonGreen
                    )
                }
                Divider(color = Color.Gray.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun TablaEnemigos(enemigos: List<EnemyKilled>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), 
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // El título ocupa el espacio de la primera y segunda columna
                Text("Enemigo Destruidos", modifier = Modifier.weight(3f), fontWeight = FontWeight.Bold, color = NeonViolet)
                Text("Cantidad", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = NeonViolet)
            }
            Divider(color = NeonViolet.copy(alpha = 0.2f))
            enemigos.forEach { enemigo ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), 
                    verticalAlignment = Alignment.CenterVertically, 
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = obtenerIdImagenEnemigo(enemigo.name)),
                        contentDescription = enemigo.name,
                        modifier = Modifier.size(60.dp).weight(1f),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = enemigo.name, 
                        modifier = Modifier.weight(2f), 
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "x${enemigo.count}", 
                        modifier = Modifier.weight(1f), 
                        fontWeight = FontWeight.Bold, 
                        color = NeonRed
                    )
                }
                Divider(color = Color.Gray.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun obtenerIdImagenTorre(nombre: String): Int {
    return when (nombre.lowercase()) {
        "tesla" -> R.drawable.tesla
        "pulso" -> R.drawable.pulso
        "laser" -> R.drawable.laser
        "perforante" -> R.drawable.perforante
        "trituradora" -> R.drawable.trituradora
        "veneno" -> R.drawable.veneno
        else -> R.drawable.ic_launcher_foreground
    }
}

@Composable
fun obtenerIdImagenEnemigo(nombre: String): Int {
    return when (nombre.lowercase()) {
        "pyxer" -> R.drawable.pyxer
        "pyxerion" -> R.drawable.pyxerion
        "pyxerarch" -> R.drawable.pyxerarch
        "velth" -> R.drawable.velth
        "velarth" -> R.drawable.velarth
        "velariath" -> R.drawable.velariath
        "zhax" -> R.drawable.zhax
        "zhaxeris" -> R.drawable.zhaxeris
        "zhaxarian" -> R.drawable.zhaxarian
        else -> R.drawable.ic_launcher_foreground
    }
}

@Composable
fun obtenerColorEstadoSimple(status: GameStatus): Color {
    return when (status) {
        GameStatus.VICTORIA -> NeonBlue
        GameStatus.DERROTA -> NeonViolet
        GameStatus.RENDICION -> TextGray
        GameStatus.INFINITO -> NeonYellow
    }
}

@Composable
fun CabeceraMenuLateral(nombreUsuario: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Brush.verticalGradient(colors = listOf(NeonBlue.copy(alpha = 0.2f), Color.Transparent))), 
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(64.dp).background(NeonBlue, shape = RoundedCornerShape(32.dp)), 
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (nombreUsuario.isNotEmpty()) nombreUsuario.take(1).uppercase() else "?", 
                    style = MaterialTheme.typography.headlineMedium, 
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(nombreUsuario, style = MaterialTheme.typography.titleLarge)
        }
    }
}