package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.ui.components.TeamListItem // Import TeamListItem
import com.nextmatch.app.viewmodel.TeamViewModel


@Composable
fun TeamsSelectionScreen(
    navController: NavController,
    teamViewModel: TeamViewModel // teamViewModel is now provided by AppNavigation
) {
    val uiState by teamViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
    ) {
        // Header con fondo verde neón
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.neon_green))
                .padding(16.dp)
        ) {
            Text(
                text = "Buscando equipos...",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // Título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Equipos Disponibles",
                style = MaterialTheme.typography.headlineMedium,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { teamViewModel.refreshTeams() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Actualizar equipos")
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(R.color.neon_green)
                    )
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "",
                        color = colorResource(R.color.error_red),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.teams.isEmpty() -> {
                    Text(
                        text = "No hay equipos registrados",
                        color = colorResource(R.color.text_medium_gray),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.teams, key = { it.id }) { teamEntity ->
                            TeamListItem( // Use the generic TeamListItem
                                team = teamEntity,
                                selected = false, // Always false for read-only view
                                onSelect = { /* No action on select in this view */ }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("my_teams") }, // Navigate to MyTeamsScreenCompose
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Administrar Mis Equipos",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
