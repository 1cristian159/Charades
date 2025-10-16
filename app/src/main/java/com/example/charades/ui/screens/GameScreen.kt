package com.example.charades.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.data.Category
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    category: Category,
    currentWord: String,
    timeRemaining: Int,
    team1Score: Int,
    team2Score: Int,
    currentTeam: Int,
    roundNumber: Int,
    totalRounds: Int,
    isGameActive: Boolean,
    isTimeUp: Boolean,
    onStartGame: () -> Unit,
    onCorrectAnswer: () -> Unit,
    onSkipWord: () -> Unit,
    onPauseResume: () -> Unit,
    onFinishGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = when {
            timeRemaining <= 10 -> Color.Red
            timeRemaining <= 20 -> Color(0xFFFF9800)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(500),
        label = "timer_color"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con información del juego
        GameHeader(
            category = category,
            team1Score = team1Score,
            team2Score = team2Score,
            currentTeam = currentTeam,
            roundNumber = roundNumber,
            totalRounds = totalRounds
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Temporizador circular
        CircularTimer(
            timeRemaining = timeRemaining,
            totalTime = 60,
            color = animatedColor,
            isTimeUp = isTimeUp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Palabra actual
        WordDisplay(
            word = currentWord,
            isTimeUp = isTimeUp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Controles del juego
        GameControls(
            isGameActive = isGameActive,
            isTimeUp = isTimeUp,
            onStartGame = onStartGame,
            onCorrectAnswer = onCorrectAnswer,
            onSkipWord = onSkipWord,
            onPauseResume = onPauseResume,
            onFinishGame = onFinishGame
        )
    }
}

@Composable
fun GameHeader(
    category: Category,
    team1Score: Int,
    team2Score: Int,
    currentTeam: Int,
    roundNumber: Int,
    totalRounds: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TeamScore(
                    teamName = "Equipo 1",
                    score = team1Score,
                    isActive = currentTeam == 1
                )
                TeamScore(
                    teamName = "Equipo 2", 
                    score = team2Score,
                    isActive = currentTeam == 2
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Ronda $roundNumber de $totalRounds",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TeamScore(
    teamName: String,
    score: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = teamName,
            fontSize = 14.sp,
            color = if (isActive) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = score.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CircularTimer(
    timeRemaining: Int,
    totalTime: Int,
    color: Color,
    isTimeUp: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = (totalTime - timeRemaining).toFloat() / totalTime.toFloat()
    
    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            color = color,
            strokeWidth = 8.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isTimeUp) "¡TIEMPO!" else timeRemaining.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTimeUp) Color.Red else color
            )
            Text(
                text = "segundos",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun WordDisplay(
    word: String,
    isTimeUp: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isTimeUp) Color.Red.copy(alpha = 0.1f)
                           else MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isTimeUp) "¡TIEMPO AGOTADO!" else word,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTimeUp) Color.Red 
                       else MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GameControls(
    isGameActive: Boolean,
    isTimeUp: Boolean,
    onStartGame: () -> Unit,
    onCorrectAnswer: () -> Unit,
    onSkipWord: () -> Unit,
    onPauseResume: () -> Unit,
    onFinishGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            !isGameActive && !isTimeUp -> {
                // Botón de inicio
                Button(
                    onClick = onStartGame,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Comenzar Juego")
                }
            }
            isTimeUp -> {
                // Botón para ver resultados
                Button(
                    onClick = onFinishGame,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ver Resultados")
                }
            }
            else -> {
                // Controles durante el juego
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onCorrectAnswer,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Correcto")
                    }
                    
                    Button(
                        onClick = onSkipWord,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800)
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Saltar")
                    }
                }
                
                Button(
                    onClick = onPauseResume,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isGameActive) "Pausar" else "Reanudar")
                }
            }
        }
    }
}
