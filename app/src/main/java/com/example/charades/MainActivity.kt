package com.example.charades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.charades.data.Category
import com.example.charades.ui.screens.CategorySelectionScreen
import com.example.charades.ui.screens.GameScreen
import com.example.charades.ui.screens.ResultsScreen
import com.example.charades.ui.theme.CharadesTheme
import com.example.charades.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharadesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharadesApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CharadesApp(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    
    when {
        gameState.currentCategory == null -> {
            // Pantalla de selección de categorías
            CategorySelectionScreen(
                onCategorySelected = { category ->
                    viewModel.selectCategory(category)
                },
                modifier = modifier
            )
        }
        gameState.gameFinished -> {
            // Pantalla de resultados
            ResultsScreen(
                team1Score = gameState.team1Score,
                team2Score = gameState.team2Score,
                totalRounds = gameState.totalRounds,
                onRestartGame = {
                    viewModel.restartGame()
                },
                onBackToMenu = {
                    viewModel.resetToMenu()
                },
                modifier = modifier
            )
        }
        else -> {
            // Pantalla de juego
            GameScreen(
                category = gameState.currentCategory!!,
                currentWord = gameState.currentWord,
                timeRemaining = gameState.timeRemaining,
                team1Score = gameState.team1Score,
                team2Score = gameState.team2Score,
                currentTeam = gameState.currentTeam,
                roundNumber = gameState.roundNumber,
                totalRounds = gameState.totalRounds,
                isGameActive = gameState.isGameActive,
                isTimeUp = gameState.isTimeUp,
                onStartGame = {
                    viewModel.startGame()
                },
                onCorrectAnswer = {
                    viewModel.correctAnswer()
                },
                onSkipWord = {
                    viewModel.skipWord()
                },
                onPauseResume = {
                    viewModel.pauseResumeGame()
                },
                onFinishGame = {
                    viewModel.finishGame()
                },
                modifier = modifier
            )
        }
    }
}