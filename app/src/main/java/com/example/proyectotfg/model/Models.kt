package com.example.proyectotfg.model

enum class GameStatus(val label: String) {
    VICTORIA("victoria"),
    DERROTA("derrota"),
    RENDICION("rendicion"),

    INFINITO("infinito")
}

enum class Difficulty(val label: String) {
    FACIL("facil"),
    NORMAL("normal"),
    DIFICIL("dificil")
}

data class User(
    val id: Int,
    val nickname: String
)

data class GameStats(
    val damageDealt: Int,
    val damageReceived: Int,
    val goldEarned: Int,
    val goldSpent: Int,
    val rounds: Int
)

data class TowerUsed(
    val name: String,
    val count: Int
)

data class EnemyKilled(
    val name: String,
    val count: Int
)

data class Game(
    val id: Int,
    val date: String,
    val status: GameStatus,
    val difficulty: Difficulty,
    val stats: GameStats,
    val towersUsed: List<TowerUsed>,
    val enemiesKilled: List<EnemyKilled>
)