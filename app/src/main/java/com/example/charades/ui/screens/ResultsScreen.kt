package com.example.charades.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de resultados finales del juego.
 *
 * Muestra el equipo ganador (o empate), las puntuaciones finales, estadísticas
 * generales y acciones para reiniciar o volver al menú.
 */
@Composable
fun ResultsScreen(
    team1Score: Int,
    team2Score: Int,
    totalRounds: Int,
    onRestartGame: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val winner = when {
        team1Score > team2Score -> 1
        team2Score > team1Score -> 2
        else -> 0 // Empate
    }

    val animatedTeam1Score = team1Score
    val animatedTeam2Score = team2Score

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "RESULTADOS FINALES",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Juego completado",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Ganador
        if (winner != 0) {
            WinnerCard(
                winnerTeam = winner,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        } else {
            TieCard(
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        // Puntuaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreCard(
                teamName = "Equipo 1",
                score = animatedTeam1Score,
                isWinner = winner == 1,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            ScoreCard(
                teamName = "Equipo 2",
                score = animatedTeam2Score,
                isWinner = winner == 2,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Estadísticas
        StatsCard(
            totalRounds = totalRounds,
            team1Score = team1Score,
            team2Score = team2Score
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de acción
        ActionButtons(
            onRestartGame = onRestartGame,
            onBackToMenu = onBackToMenu
        )
    }
}

/**
 * Tarjeta que muestra el equipo ganador.
 */
@Composable
fun WinnerCard(
    winnerTeam: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD700).copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Equipo $winnerTeam ganador",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }
    }
}

/**
 * Tarjeta que indica un resultado de empate.
 */
@Composable
fun TieCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Empate",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ScoreCard(
    teamName: String,
    score: Int,
    isWinner: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner) Color(0xFFFFD700).copy(alpha = 0.1f)
                           else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isWinner) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = teamName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isWinner) Color(0xFFFFD700) 
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = score.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWinner) Color(0xFFFFD700) 
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "puntos",
                fontSize = 12.sp,
                color = if (isWinner) Color(0xFFFFD700).copy(alpha = 0.7f)
                       else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun StatsCard(
    totalRounds: Int,
    team1Score: Int,
    team2Score: Int,
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
                text = "Estadísticas del Juego",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Rondas",
                    value = totalRounds.toString()
                )
                StatItem(
                    label = "Total Puntos",
                    value = (team1Score + team2Score).toString()
                )
                StatItem(
                    label = "Promedio",
                    value = String.format("%.1f", (team1Score + team2Score).toFloat() / totalRounds)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActionButtons(
    onRestartGame: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onRestartGame,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Jugar de Nuevo")
        }
        
        OutlinedButton(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver al Menú")
        }
    }
}
