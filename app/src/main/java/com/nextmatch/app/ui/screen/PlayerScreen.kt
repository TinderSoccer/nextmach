package com.nextmatch.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.UUID
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.viewmodel.PlayerViewModel
import com.nextmatch.app.data.entities.PlayerEntity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults


@Composable
fun PlayerScreen(
    navController: NavController,
    playerViewModel: PlayerViewModel // playerViewModel is now provided by AppNavigation
) {
    val uiState by playerViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        playerViewModel.refreshPlayers(null)
    }

    var selectedPlayer by remember { mutableStateOf<PlayerEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

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
                text = "Gestionando jugadores...",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // Título
        Text(
            text = "Jugadores",
            style = MaterialTheme.typography.headlineMedium,
            color = colorResource(R.color.text_white),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )

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

                uiState.players.isEmpty() -> {
                    Text(
                        text = "No hay jugadores registrados",
                        color = colorResource(R.color.text_medium_gray),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.players, key = { it.id }) { playerEntity ->
                            PlayerCard(
                                player = playerEntity,
                                selected = selectedPlayer?.id == playerEntity.id,
                                onSelect = { selectedPlayer = if (selectedPlayer?.id == playerEntity.id) null else playerEntity }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Agregar Jugador", color = colorResource(R.color.background_black), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { showEditDialog = true },
                enabled = selectedPlayer != null,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Editar Jugador", color = colorResource(R.color.neon_green), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    selectedPlayer?.let { playerViewModel.deletePlayer(it) }
                    selectedPlayer = null
                },
                enabled = selectedPlayer != null,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.error_red)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Eliminar", color = colorResource(R.color.text_white), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showAddDialog) {
        PlayerDialog(
            player = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { newPlayer ->
                playerViewModel.insertPlayer(newPlayer)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedPlayer != null) {
        PlayerDialog(
            player = selectedPlayer,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedPlayer ->
                playerViewModel.updatePlayer(updatedPlayer)
                showEditDialog = false
                selectedPlayer = null // Deselect after editing
            }
        )
    }
}

@Composable
private fun PlayerCard(player: PlayerEntity, selected: Boolean, onSelect: () -> Unit) {
    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) colorResource(R.color.surface_dark) else colorResource(R.color.background_black)
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (selected) {
            BorderStroke(1.dp, colorResource(R.color.neon_green))
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(player.nombre, color = colorResource(R.color.text_white), fontWeight = FontWeight.SemiBold)
                player.posicion?.let {
                    Text(it, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                }
            }
            Button(
                onClick = onSelect,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) colorResource(R.color.neon_green) else colorResource(R.color.background_black)
                )
            ) {
                if (selected) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Seleccionado",
                        tint = colorResource(R.color.background_black)
                    )
                } else {
                    Text("Seleccionar", color = colorResource(R.color.text_white), fontSize = 12.sp)
                }
            }
        }
    }
}

// Dialog Composable para Agregar/Editar Jugador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDialog(
    player: PlayerEntity?, // null for add, PlayerEntity for edit
    onDismiss: () -> Unit,
    onConfirm: (PlayerEntity) -> Unit
) {
    var id by remember { mutableStateOf(player?.id ?: UUID.randomUUID().toString()) }
    var nombre by remember { mutableStateOf(player?.nombre ?: "") }
    var correo by remember { mutableStateOf(player?.correo ?: "") }
    var posicion by remember { mutableStateOf(player?.posicion ?: "") }
    var nivel by remember { mutableStateOf(player?.nivel ?: "Principiante") }
    var telefono by remember { mutableStateOf(player?.telefono ?: "") }
    var equipoId by remember { mutableStateOf(player?.equipoId ?: "") }
    var activo by remember { mutableStateOf(player?.activo ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (player == null) "Agregar Nuevo Jugador" else "Editar Jugador", color = colorResource(R.color.text_white)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Jugador") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = posicion,
                    onValueChange = { posicion = it },
                    label = { Text("Posición") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = nivel,
                    onValueChange = { nivel = it },
                    label = { Text("Nivel") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = equipoId,
                    onValueChange = { equipoId = it },
                    label = { Text("ID del Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Activo", color = colorResource(R.color.text_white))
                    Switch(
                        checked = activo,
                        onCheckedChange = { activo = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorResource(R.color.neon_green),
                            checkedTrackColor = colorResource(R.color.neon_green).copy(alpha = 0.5f),
                            uncheckedThumbColor = colorResource(R.color.text_medium_gray),
                            uncheckedTrackColor = colorResource(R.color.text_medium_gray).copy(alpha = 0.5f),
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        PlayerEntity(
                            id = id,
                            nombre = nombre,
                            correo = correo.ifEmpty { null },
                            posicion = posicion.ifEmpty { null },
                            nivel = nivel.ifEmpty { null },
                            telefono = telefono.ifEmpty { null },
                            equipoId = equipoId.ifEmpty { null },
                            activo = activo
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
            ) {
                Text("Guardar", color = colorResource(R.color.background_black))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar", color = colorResource(R.color.text_white))
            }
        },
        containerColor = colorResource(R.color.background_black)
    )
}
