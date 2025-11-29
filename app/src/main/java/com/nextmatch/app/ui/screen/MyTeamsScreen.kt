package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Import sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.data.entities.TeamEntity
import com.nextmatch.app.ui.components.TeamDialog // Import TeamDialog
import com.nextmatch.app.ui.components.TeamListItem // Import TeamListItem
import com.nextmatch.app.viewmodel.AuthViewModel
import com.nextmatch.app.viewmodel.TeamViewModel
import java.util.UUID
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Import ArrowBack
import androidx.compose.material.icons.filled.Refresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTeamsScreenCompose(
    navController: NavController,
    authViewModel: AuthViewModel, // We need AuthViewModel to get the current user's ID
    teamViewModel: TeamViewModel // teamViewModel is now provided by AppNavigation
) {
    val uiState by teamViewModel.uiState.collectAsState()
    val authState by authViewModel.estado.collectAsState()
    val currentUserId = authState.usuarioActual?.id // Get the authenticated user's ID
    val ownedTeams = remember(uiState.teams, currentUserId) {
        if (currentUserId.isNullOrBlank()) {
            emptyList()
        } else {
            uiState.teams.filter { it.userId == currentUserId }
        }
    }
    val nextMatchTeams = remember(uiState.teams, currentUserId) {
        if (uiState.teams.isEmpty()) emptyList()
        else uiState.teams.filter { team ->
            currentUserId.isNullOrBlank() || team.userId != currentUserId
        }
    }

    var selectedTeam by remember { mutableStateOf<TeamEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(ownedTeams) {
        if (selectedTeam != null && ownedTeams.none { it.id == selectedTeam?.id }) {
            selectedTeam = null
        }
    }

    // Make sure we only fetch my teams
    LaunchedEffect(currentUserId) {
        if (currentUserId != null) {
            // TeamViewModel.getMyTeams() is already called in init, but this ensures it's fresh
            // No direct call here, as init handles the initial fetch.
            // If we want to refresh on this screen, we'd add a refresh mechanism in ViewModel
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
    ) {
        // Header
        TopAppBar(
            title = { Text("Mis Equipos") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            },
            actions = {
                IconButton(onClick = { teamViewModel.refreshTeams() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar equipos")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.surface_dark),
                titleContentColor = colorResource(R.color.text_white)
            )
        )

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
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

                ownedTeams.isNotEmpty() || nextMatchTeams.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (ownedTeams.isNotEmpty()) {
                            items(ownedTeams, key = { it.id }) { teamEntity ->
                                TeamListItem(
                                    team = teamEntity,
                                    selected = selectedTeam?.id == teamEntity.id,
                                    onSelect = { selectedTeam = if (selectedTeam?.id == teamEntity.id) null else teamEntity }
                                )
                            }
                        } else if (currentUserId.isNullOrBlank()) {
                            item {
                                Text(
                                    text = "Inicia sesión para administrar tus equipos.",
                                    color = colorResource(R.color.text_medium_gray)
                                )
                            }
                        }

                        if (nextMatchTeams.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Equipos de NextMatch",
                                    color = colorResource(R.color.text_medium_gray),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(nextMatchTeams, key = { it.id }) { teamEntity ->
                                TeamListItem(
                                    team = teamEntity,
                                    selected = false,
                                    onSelect = {
                                        navController.navigate("team_profile/${teamEntity.id}")
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No hay equipos disponibles todavía.",
                        color = colorResource(R.color.text_medium_gray),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Agregar Equipo", color = colorResource(R.color.background_black), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { showEditDialog = true },
                enabled = selectedTeam != null,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Editar", color = colorResource(R.color.neon_green), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    selectedTeam?.let { teamViewModel.deleteTeam(it) }
                    selectedTeam = null
                },
                enabled = selectedTeam != null,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.error_red)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Eliminar", color = colorResource(R.color.text_white), fontSize = 14.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    selectedTeam?.let { team ->
                        navController.navigate("team_profile/${team.id}")
                    }
                },
                enabled = selectedTeam != null,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Ver jugadores", color = colorResource(R.color.neon_green), fontSize = 14.sp)
            }
        }
    }

    if (showAddDialog) {
        TeamDialog(
            team = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { newTeam ->
                currentUserId?.let { userId ->
                    teamViewModel.insertTeam(newTeam.copy(userId = userId)) // Assign current user's ID
                } ?: run {
                    // Handle error: user ID not available
                    // For now, just log or show a toast
                    println("Error: User ID not available for creating team.")
                }
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedTeam != null) {
        TeamDialog(
            team = selectedTeam,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedTeam ->
                currentUserId?.let { userId ->
                    teamViewModel.updateTeam(updatedTeam.copy(userId = userId)) // Ensure userId is preserved/updated
                } ?: run {
                    println("Error: User ID not available for updating team.")
                }
                showEditDialog = false
                selectedTeam = null // Deselect after editing
            }
        )
    }
}
