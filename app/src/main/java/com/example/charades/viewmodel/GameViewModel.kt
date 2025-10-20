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

/**
 * ViewModel que gestiona la lógica del juego y el temporizador.
 *
 * Expone un flujo de [GameState] para que la UI pueda reaccionar a
 * los cambios. Controla la categoría, la palabra actual, el tiempo,
 * las puntuaciones y el avance de rondas.
 */
class GameViewModel : ViewModel() {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private var timerJob: Job? = null
    private val totalTime = 60 // 1 minuto por ronda
    private val totalRounds = 10
    
    /** Selecciona la categoría inicial y prepara la primera ronda. */
    fun selectCategory(category: Category) {
        _gameState.value = _gameState.value.copy(
            currentCategory = category,
            currentWord = GameRepository.getRandomWord(category.id) ?: "",
            timeRemaining = totalTime,
            isGameActive = false,
            roundNumber = 1,
            totalRounds = totalRounds,
            isTimeUp = false,
            gameFinished = false,
            team1PlayedThisRound = false,
            team2PlayedThisRound = false
        )
    }
    
    /** Inicia el conteo del temporizador de la ronda actual. */
    fun startGame() {
        if (_gameState.value.currentCategory != null) {
            _gameState.value = _gameState.value.copy(
                isGameActive = true,
                isTimeUp = false
            )
            startTimer()
        }
    }
    
    /** Alterna entre pausar y reanudar el temporizador. */
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
    
    /** Registra una respuesta correcta y avanza a la siguiente palabra y equipo. */
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
            
            // Marcar que el equipo actual ya jugó esta ronda
            val updatedState = if (currentState.currentTeam == 1) {
                currentState.copy(team1PlayedThisRound = true)
            } else {
                currentState.copy(team2PlayedThisRound = true)
            }
            
            // Verificar si ambos equipos han jugado esta ronda
            val bothTeamsPlayed = updatedState.team1PlayedThisRound && updatedState.team2PlayedThisRound
            
            if (bothTeamsPlayed) {
                // Ambos equipos han jugado - avanzar a la siguiente ronda
                val nextRound = currentState.roundNumber + 1
                
                if (nextRound <= totalRounds) {
                    // Nueva ronda - resetear flags y empezar con equipo 1
                    _gameState.value = updatedState.copy(
                        team1Score = newTeam1Score,
                        team2Score = newTeam2Score,
                        roundNumber = nextRound,
                        currentTeam = 1,
                        currentWord = getNextWord(),
                        timeRemaining = totalTime,
                        isTimeUp = false,
                        team1PlayedThisRound = false,
                        team2PlayedThisRound = false
                    )
                } else {
                    // Juego terminado
                    _gameState.value = updatedState.copy(
                        team1Score = newTeam1Score,
                        team2Score = newTeam2Score,
                        gameFinished = true,
                        isGameActive = false
                    )
                }
            } else {
                // Solo un equipo ha jugado - cambiar al otro equipo
                _gameState.value = updatedState.copy(
                    team1Score = newTeam1Score,
                    team2Score = newTeam2Score,
                    currentTeam = nextTeam,
                    currentWord = nextWord,
                    timeRemaining = totalTime
                )
            }
            
            // Reiniciar temporizador
            timerJob?.cancel()
            startTimer()
        }
    }
    
    /** Omite la palabra actual y cede el turno al otro equipo. */
    fun skipWord() {
        val currentState = _gameState.value
        if (currentState.isGameActive && !currentState.isTimeUp) {
            // Cambiar de equipo y obtener nueva palabra
            val nextTeam = if (currentState.currentTeam == 1) 2 else 1
            val nextWord = getNextWord()
            
            // Marcar que el equipo actual ya jugó esta ronda
            val updatedState = if (currentState.currentTeam == 1) {
                currentState.copy(team1PlayedThisRound = true)
            } else {
                currentState.copy(team2PlayedThisRound = true)
            }
            
            // Verificar si ambos equipos han jugado esta ronda
            val bothTeamsPlayed = updatedState.team1PlayedThisRound && updatedState.team2PlayedThisRound
            
            if (bothTeamsPlayed) {
                // Ambos equipos han jugado - avanzar a la siguiente ronda
                val nextRound = currentState.roundNumber + 1
                
                if (nextRound <= totalRounds) {
                    // Nueva ronda - resetear flags y empezar con equipo 1
                    _gameState.value = updatedState.copy(
                        roundNumber = nextRound,
                        currentTeam = 1,
                        currentWord = getNextWord(),
                        timeRemaining = totalTime,
                        isTimeUp = false,
                        team1PlayedThisRound = false,
                        team2PlayedThisRound = false
                    )
                } else {
                    // Juego terminado
                    _gameState.value = updatedState.copy(
                        gameFinished = true,
                        isGameActive = false
                    )
                }
            } else {
                // Solo un equipo ha jugado - cambiar al otro equipo
                _gameState.value = updatedState.copy(
                    currentTeam = nextTeam,
                    currentWord = nextWord,
                    timeRemaining = totalTime
                )
            }
            
            // Reiniciar temporizador
            timerJob?.cancel()
            startTimer()
        }
    }
    
    /** Finaliza el juego y muestra los resultados. */
    fun finishGame() {
        timerJob?.cancel()
        _gameState.value = _gameState.value.copy(
            gameFinished = true,
            isGameActive = false
        )
    }
    
    /** Reinicia el juego manteniendo la misma categoría. */
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
                gameFinished = false,
                team1PlayedThisRound = false,
                team2PlayedThisRound = false
            )
        }
    }
    
    /** Restablece el estado para volver al menú de categorías. */
    fun resetToMenu() {
        timerJob?.cancel()
        _gameState.value = GameState()
    }
    
    /** Lanza una corrutina que decrementa el temporizador cada segundo. */
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
                        // Marcar que el equipo actual ya jugó esta ronda
                        val updatedState = if (currentState.currentTeam == 1) {
                            currentState.copy(team1PlayedThisRound = true)
                        } else {
                            currentState.copy(team2PlayedThisRound = true)
                        }
                        
                        // Verificar si ambos equipos han jugado esta ronda
                        val bothTeamsPlayed = updatedState.team1PlayedThisRound && updatedState.team2PlayedThisRound
                        
                        if (bothTeamsPlayed) {
                            // Ambos equipos han jugado - avanzar a la siguiente ronda
                            val nextRound = currentState.roundNumber + 1
                            
                            if (nextRound <= totalRounds) {
                                // Nueva ronda - resetear flags y empezar con equipo 1
                                _gameState.value = updatedState.copy(
                                    roundNumber = nextRound,
                                    currentTeam = 1,
                                    currentWord = getNextWord(),
                                    timeRemaining = totalTime,
                                    isTimeUp = false,
                                    team1PlayedThisRound = false,
                                    team2PlayedThisRound = false
                                )
                                delay(2000) // Pausa de 2 segundos antes de continuar
                                startTimer()
                            } else {
                                // Juego terminado
                                _gameState.value = updatedState.copy(
                                    gameFinished = true,
                                    isGameActive = false
                                )
                            }
                        } else {
                            // Solo un equipo ha jugado - cambiar al otro equipo
                            val nextTeam = if (currentState.currentTeam == 1) 2 else 1
                            _gameState.value = updatedState.copy(
                                currentTeam = nextTeam,
                                currentWord = getNextWord(),
                                timeRemaining = totalTime,
                                isTimeUp = false
                            )
                            delay(2000) // Pausa de 2 segundos antes de continuar
                            startTimer()
                        }
                        break
                    }
                }
            }
        }
    }
    
    /** Obtiene la siguiente palabra de la categoría actual. */
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
