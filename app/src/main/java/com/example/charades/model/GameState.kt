package com.example.charades.model

import com.example.charades.data.Category

/**
 * Estado inmutable del juego de charadas.
 *
 * Se utiliza como única fuente de verdad para la UI. Cualquier cambio
 * en el estado se refleja en pantalla al recolectar el flujo expuesto
 * por el ViewModel.
 */
data class GameState(
    val currentCategory: Category? = null,
    val currentWord: String = "",
    val timeRemaining: Int = 60,
    val isGameActive: Boolean = false,
    val team1Score: Int = 0,
    val team2Score: Int = 0,
    val currentTeam: Int = 1, // 1 o 2
    val roundNumber: Int = 1,
    val totalRounds: Int = 10,
    val isTimeUp: Boolean = false,
    val gameFinished: Boolean = false,
    val team1PlayedThisRound: Boolean = false,
    val team2PlayedThisRound: Boolean = false
)
