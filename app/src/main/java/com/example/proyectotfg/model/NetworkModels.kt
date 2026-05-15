package com.example.proyectotfg.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la solicitud de inicio de sesión enviada a la API.
 */
data class LoginRequest(
    val nickname: String,
    val contrasena: String
)

/**
 * Representa la respuesta completa de la API tras un login exitoso.
 */
data class LoginResponse(
    @SerializedName("usuario") val nickname: String,
    val partidas: List<GameNetwork>
)

/**
 * Representa una partida individual dentro del historial del usuario.
 */
data class GameNetwork(
    @SerializedName("id_partida") val id: Int,
    val fecha: String,
    val estado: String,
    val nivel: String,
    @SerializedName("estadisticas") val stats: GameStatsNetwork,
    @SerializedName("torres") val towersUsed: List<TowerUsedNetwork>,
    @SerializedName("enemigos") val enemiesKilled: List<EnemyKilledNetwork>
)

/**
 * Estadísticas generales de una partida.
 */
data class GameStatsNetwork(
    @SerializedName("dano_hecho") val damageDealt: Int,
    @SerializedName("dano_recibido") val damageReceived: Int,
    @SerializedName("oro_ganado") val goldEarned: Int,
    @SerializedName("oro_gastado") val goldSpent: Int,
    @SerializedName("ronda") val rounds: Int
)

/**
 * Información sobre una torre específica utilizada en la partida.
 */
data class TowerUsedNetwork(
    @SerializedName("nombre") val name: String,
    @SerializedName("cantidad") val count: Int
)

/**
 * Información sobre un tipo de enemigo eliminado en la partida.
 */
data class EnemyKilledNetwork(
    @SerializedName("nombre") val name: String,
    @SerializedName("cantidad") val count: Int
)