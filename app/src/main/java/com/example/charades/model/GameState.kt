package com.example.charades.model

import com.example.charades.data.Category

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
    val gameFinished: Boolean = false
)
