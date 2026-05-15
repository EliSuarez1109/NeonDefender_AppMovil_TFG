package com.example.proyectotfg.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectotfg.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel que gestiona las estadísticas del jugador y el historial de partidas.
 * Procesa los datos recibidos de la API para adaptarlos a la interfaz de usuario.
 */
class HomeViewModel : ViewModel() {
    // Flujo de estado para la lista de partidas procesadas
    private val _partidas = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _partidas.asStateFlow()

    // Flujo de estado para la partida que se visualiza actualmente
    private val _partidaSeleccionada = MutableStateFlow<Game?>(null)
    val selectedGame: StateFlow<Game?> = _partidaSeleccionada.asStateFlow()

    // Apodo del usuario actual
    private val _apodoUsuario = MutableStateFlow("")
    val nickname: StateFlow<String> = _apodoUsuario.asStateFlow()

    /**
     * Procesa la respuesta del servidor y mapea los datos de red a objetos de dominio.
     * 
     * @param respuestaLogin Objeto con la información del usuario y sus partidas desde AWS.
     */
    fun setInitialData(respuestaLogin: LoginResponse) {
        _apodoUsuario.value = respuestaLogin.nickname
        
        // Mapeo de la lista de partidas de red a la lógica interna de la app
        val listaPartidasMapeadas = respuestaLogin.partidas.map { partidaRed ->
            // Mapeo del estado de la partida
            val estado = when (partidaRed.estado.lowercase()) {
                "victoria" -> GameStatus.VICTORIA
                "derrota" -> GameStatus.DERROTA
                "rendicion" -> GameStatus.RENDICION
                else -> GameStatus.INFINITO
            }
            
            // Mapeo de la dificultad
            val dificultad = when (partidaRed.nivel.lowercase()) {
                "facil" -> Difficulty.FACIL
                "normal" -> Difficulty.NORMAL
                else -> Difficulty.DIFICIL
            }
            
            // Creación del objeto Game listo para la UI
            Game(
                id = partidaRed.id,
                date = partidaRed.fecha,
                status = estado,
                difficulty = dificultad,
                stats = GameStats(
                    damageDealt = partidaRed.stats.damageDealt,
                    damageReceived = partidaRed.stats.damageReceived,
                    goldEarned = partidaRed.stats.goldEarned,
                    goldSpent = partidaRed.stats.goldSpent,
                    rounds=partidaRed.stats.rounds
                ),
                towersUsed = partidaRed.towersUsed.map { TowerUsed(it.name, it.count) },
                enemiesKilled = partidaRed.enemiesKilled.map { EnemyKilled(it.name, it.count) }
            )
        }
        
        _partidas.value = listaPartidasMapeadas
        
        // Seleccionamos la primera partida por defecto si no hay ninguna seleccionada
        if (_partidaSeleccionada.value == null) {
            _partidaSeleccionada.value = listaPartidasMapeadas.firstOrNull()
        }
    }

    /**
     * Actualiza la partida activa que se muestra en el detalle.
     * 
     * @param partida Objeto de partida seleccionado por el usuario.
     */
    fun selectGame(partida: Game) {
        _partidaSeleccionada.value = partida
    }
}