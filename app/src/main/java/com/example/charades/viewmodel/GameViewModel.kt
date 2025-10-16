package com.example.charades.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charades.data.GameRepository
import com.example.charades.data.Category
import com.example.charades.model.GameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private var timerJob: Job? = null
    private val totalTime = 60 // 1 minuto por ronda
    private val totalRounds = 10
    
    fun selectCategory(category: Category) {
        _gameState.value = _gameState.value.copy(
            currentCategory = category,
            currentWord = GameRepository.getRandomWord(category.id) ?: "",
            timeRemaining = totalTime,
            isGameActive = false,
            roundNumber = 1,
            totalRounds = totalRounds,
            isTimeUp = false,
            gameFinished = false
        )
    }
    
    fun startGame() {
        if (_gameState.value.currentCategory != null) {
            _gameState.value = _gameState.value.copy(
                isGameActive = true,
                isTimeUp = false
            )
            startTimer()
        }
    }
    
    fun pauseResumeGame() {
        val currentState = _gameState.value
        if (currentState.isGameActive) {
            // Pausar
            _gameState.value = currentState.copy(isGameActive = false)
            timerJob?.cancel()
        } else {
            // Reanudar
            _gameState.value = currentState.copy(isGameActive = true)
            startTimer()
        }
    }
    
    fun correctAnswer() {
        val currentState = _gameState.value
        if (currentState.isGameActive && !currentState.isTimeUp) {
            // Sumar punto al equipo actual
            val newTeam1Score = if (currentState.currentTeam == 1) 
                currentState.team1Score + 1 else currentState.team1Score
            val newTeam2Score = if (currentState.currentTeam == 2) 
                currentState.team2Score + 1 else currentState.team2Score
            
            // Cambiar de equipo y obtener nueva palabra
            val nextTeam = if (currentState.currentTeam == 1) 2 else 1
            val nextWord = getNextWord()
            
            _gameState.value = currentState.copy(
                team1Score = newTeam1Score,
                team2Score = newTeam2Score,
                currentTeam = nextTeam,
                currentWord = nextWord,
                timeRemaining = totalTime
            )
            
            // Reiniciar temporizador
            timerJob?.cancel()
            startTimer()
        }
    }
    
    fun skipWord() {
        val currentState = _gameState.value
        if (currentState.isGameActive && !currentState.isTimeUp) {
            // Cambiar de equipo y obtener nueva palabra
            val nextTeam = if (currentState.currentTeam == 1) 2 else 1
            val nextWord = getNextWord()
            
            _gameState.value = currentState.copy(
                currentTeam = nextTeam,
                currentWord = nextWord,
                timeRemaining = totalTime
            )
            
            // Reiniciar temporizador
            timerJob?.cancel()
            startTimer()
        }
    }
    
    fun finishGame() {
        timerJob?.cancel()
        _gameState.value = _gameState.value.copy(
            gameFinished = true,
            isGameActive = false
        )
    }
    
    fun restartGame() {
        timerJob?.cancel()
        val currentCategory = _gameState.value.currentCategory
        if (currentCategory != null) {
            _gameState.value = GameState(
                currentCategory = currentCategory,
                currentWord = GameRepository.getRandomWord(currentCategory.id) ?: "",
                timeRemaining = totalTime,
                isGameActive = false,
                team1Score = 0,
                team2Score = 0,
                currentTeam = 1,
                roundNumber = 1,
                totalRounds = totalRounds,
                isTimeUp = false,
                gameFinished = false
            )
        }
    }
    
    fun resetToMenu() {
        timerJob?.cancel()
        _gameState.value = GameState()
    }
    
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_gameState.value.timeRemaining > 0 && _gameState.value.isGameActive) {
                delay(1000)
                val currentState = _gameState.value
                if (currentState.isGameActive) {
                    val newTimeRemaining = currentState.timeRemaining - 1
                    _gameState.value = currentState.copy(
                        timeRemaining = newTimeRemaining,
                        isTimeUp = newTimeRemaining == 0
                    )
                    
                    if (newTimeRemaining == 0) {
                        // Tiempo agotado - cambiar de equipo
                        val nextTeam = if (currentState.currentTeam == 1) 2 else 1
                        val nextWord = getNextWord()
                        val nextRound = currentState.roundNumber + 1
                        
                        _gameState.value = _gameState.value.copy(
                            currentTeam = nextTeam,
                            currentWord = nextWord,
                            timeRemaining = totalTime,
                            roundNumber = nextRound,
                            isTimeUp = false,
                            isGameActive = nextRound <= totalRounds
                        )
                        
                        if (nextRound <= totalRounds) {
                            // Continuar con la siguiente ronda
                            delay(2000) // Pausa de 2 segundos antes de continuar
                            startTimer()
                        } else {
                            // Juego terminado
                            _gameState.value = _gameState.value.copy(
                                gameFinished = true,
                                isGameActive = false
                            )
                        }
                        break
                    }
                }
            }
        }
    }
    
    private fun getNextWord(): String {
        val currentCategory = _gameState.value.currentCategory
        return if (currentCategory != null) {
            GameRepository.getRandomWord(currentCategory.id) ?: ""
        } else {
            ""
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
