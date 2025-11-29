package com.nextmatch.app.ui.screen

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import java.util.UUID // Import UUID for player ID generation
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.data.entities.PlayerEntity // Import PlayerEntity
import com.nextmatch.app.data.entities.TeamEntity // Import TeamEntity
import com.nextmatch.app.data.remote.dto.CreateReservationRequestDto
import com.nextmatch.app.data.repository.FieldRepository
import com.nextmatch.app.model.Field
import com.nextmatch.app.ui.components.OpenStreetMapView
import com.nextmatch.app.ui.components.OsmMarker
import com.nextmatch.app.utils.GpsCalculator
import com.nextmatch.app.utils.LocationTracker
import com.nextmatch.app.viewmodel.AuthViewModel
import com.nextmatch.app.viewmodel.FieldViewModel
import com.nextmatch.app.viewmodel.FieldViewModelFactory
import com.nextmatch.app.viewmodel.PlayerViewModel // Import PlayerViewModel
import com.nextmatch.app.viewmodel.ReservationViewModel
import com.nextmatch.app.viewmodel.TeamViewModel // Import TeamViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsListScreenCompose(navController: NavController) {
    val mockTeams = listOf(
        "Equipo Rojo - ⭐⭐⭐⭐",
        "Equipo Azul - ⭐⭐⭐",
        "Equipo Verde - ⭐⭐⭐⭐⭐",
        "Equipo Amarillo - ⭐⭐",
        "Equipo Naranja - ⭐⭐⭐⭐"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos Disponibles") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp)
        ) {
            items(mockTeams) { team ->
                Button(
                    onClick = { navController.navigate("team_profile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(team, color = colorResource(R.color.text_white))
                        Text("✓", color = colorResource(R.color.neon_green))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfileScreenCompose(
    navController: NavController,
    teamId: String?, // Accept teamId as navigation argument
    authViewModel: AuthViewModel,
    teamViewModel: TeamViewModel, // teamViewModel is now provided by AppNavigation
    playerViewModel: PlayerViewModel // playerViewModel is now provided by AppNavigation
) {
    val teamState by teamViewModel.uiState.collectAsState()
    val playerState by playerViewModel.uiState.collectAsState()
    val authState by authViewModel.estado.collectAsState()

    var currentTeam by remember { mutableStateOf<TeamEntity?>(null) }
    var teamPlayers by remember { mutableStateOf<List<PlayerEntity>>(emptyList()) }

    var selectedPlayer by remember { mutableStateOf<PlayerEntity?>(null) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showEditPlayerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(teamId) {
        if (teamId != null) {
            teamViewModel.getTeamById(teamId)
            playerViewModel.refreshPlayers(teamId)
        } else {
            playerViewModel.refreshPlayers(null)
        }
    }

    LaunchedEffect(teamId, teamState.teams) {
        // Find the current team from the list if teamId is available
        currentTeam = teamState.teams.find { it.id == teamId }
    }

    LaunchedEffect(teamId, playerState.players) {
        // Filter players for the current team
        teamPlayers = playerState.players.filter { it.equipoId == teamId }
    }

    val isOwner = currentTeam?.userId?.let { it == authState.usuarioActual?.id } ?: false

    Scaffold(
        topBar = {
        TopAppBar(
                title = { Text(currentTeam?.nombre ?: "Perfil del Equipo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        teamViewModel.refreshTeams()
                        playerViewModel.refreshPlayers(teamId)
                    }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Actualizar equipo")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .verticalScroll(rememberScrollState()) // Use verticalScroll for the whole column
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.neon_green)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚽", style = MaterialTheme.typography.displayLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            currentTeam?.let { team ->
                Text(team.nombre, style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
                team.nivel?.let { Text("Nivel: $it", color = colorResource(R.color.neon_green)) }
                team.ciudad?.let { Text("Ciudad: $it", color = colorResource(R.color.text_medium_gray)) }
                team.descripcion?.let { Text("Descripción: $it", color = colorResource(R.color.text_medium_gray)) }
                team.contacto?.let { Text("Contacto: $it", color = colorResource(R.color.text_medium_gray)) }
            } ?: run {
                Text("Cargando equipo...", color = colorResource(R.color.text_white))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Player Management Section
            Text(
                text = "Jugadores del Equipo",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (teamPlayers.isEmpty()) {
                Text(
                    text = "No hay jugadores registrados en este equipo.",
                    color = colorResource(R.color.text_medium_gray),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                teamPlayers.forEach { player ->
                    PlayerCard(
                        player = player,
                        selected = selectedPlayer?.id == player.id,
                        onSelect = {
                            selectedPlayer = if (selectedPlayer?.id == player.id) null else player
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            playerState.error?.let {
                Text(
                    text = it,
                    color = colorResource(R.color.error_red),
                    modifier = Modifier.fillMaxWidth()
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            if (isOwner) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showAddPlayerDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(50.dp)
                    ) {
                        Text("Agregar Jugador", color = colorResource(R.color.background_black), fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showEditPlayerDialog = true },
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
            }

            if (isOwner && showAddPlayerDialog && teamId != null) {
                PlayerDialog(
                    player = null,
                    equipoId = teamId, // Pre-fill team ID
                    onDismiss = { showAddPlayerDialog = false },
                    onConfirm = { newPlayer ->
                        playerViewModel.insertPlayer(newPlayer)
                        showAddPlayerDialog = false
                    }
                )
            }

            if (isOwner && showEditPlayerDialog && selectedPlayer != null && teamId != null) {
                PlayerDialog(
                    player = selectedPlayer,
                    equipoId = teamId, // Pre-fill team ID
                    onDismiss = { showEditPlayerDialog = false },
                    onConfirm = { updatedPlayer ->
                        playerViewModel.updatePlayer(updatedPlayer)
                        showEditPlayerDialog = false
                        selectedPlayer = null // Deselect after editing
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Solicitar Unirse", color = colorResource(R.color.background_black))
            }
        }
    }
}

// PlayerCard Composable (copied from PlayerScreen.kt)
@Composable
private fun PlayerCard(player: PlayerEntity, selected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .safeClickable(onSelect),
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

// PlayerDialog Composable (copied from PlayerScreen.kt, adapted for equipoId)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDialog(
    player: PlayerEntity?, // null for add, PlayerEntity for edit
    equipoId: String, // Pre-filled team ID
    onDismiss: () -> Unit,
    onConfirm: (PlayerEntity) -> Unit
) {
    var id by remember { mutableStateOf(player?.id ?: UUID.randomUUID().toString()) }
    var nombre by remember { mutableStateOf(player?.nombre ?: "") }
    var correo by remember { mutableStateOf(player?.correo ?: "") }
    var posicion by remember { mutableStateOf(player?.posicion ?: "") }
    var nivel by remember { mutableStateOf(player?.nivel ?: "Principiante") }
    var telefono by remember { mutableStateOf(player?.telefono ?: "") }
    var activo by remember { mutableStateOf(player?.activo ?: true) }
    var localError by remember { mutableStateOf<String?>(null) }
    val emailMatcher = remember { android.util.Patterns.EMAIL_ADDRESS }

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
                    when {
                        nombre.isBlank() -> {
                            localError = "El nombre es obligatorio"
                            return@Button
                        }
                        equipoId.isBlank() -> {
                            localError = "Selecciona un equipo"
                            return@Button
                        }
                        correo.isNotBlank() && !emailMatcher.matcher(correo).matches() -> {
                            localError = "Correo inválido"
                            return@Button
                        }
                        else -> localError = null
                    }
                    onConfirm(
                        PlayerEntity(
                            id = id,
                            nombre = nombre,
                            correo = correo.ifEmpty { null },
                            posicion = posicion.ifEmpty { null },
                            nivel = nivel.ifEmpty { null },
                            telefono = telefono.ifEmpty { null },
                            equipoId = equipoId,
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
    localError?.let {
        Text(
            text = it,
            color = colorResource(R.color.error_red),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

// RESERVA DE CANCHAS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenCompose(
    navController: NavController,
    fieldViewModel: FieldViewModel,
    reservationViewModel: ReservationViewModel,
    teamViewModel: TeamViewModel,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val teamState by teamViewModel.uiState.collectAsState()
    val authState by authViewModel.estado.collectAsState()
    val currentUserId = authState.usuarioActual?.id
    val userTeams = remember(teamState.teams, currentUserId) {
        val teams = teamState.teams
        when {
            teams.isEmpty() -> emptyList()
            currentUserId.isNullOrBlank() -> teams
            else -> teams.filter { it.userId == currentUserId }.ifEmpty { teams }
        }
    }
    LaunchedEffect(Unit) {
        if (!fieldViewModel.uiState.isLoading && fieldViewModel.uiState.fields.isEmpty()) {
            fieldViewModel.fetchFields()
        }
    }

    val fieldState = fieldViewModel.uiState
    var selectedField by remember { mutableStateOf<Field?>(null) }
    var selectedDate by remember { mutableStateOf(currentIsoDate()) }
    val availableSlots = listOf("17:00", "18:00", "19:00", "20:00")
    var selectedSlot by remember { mutableStateOf(availableSlots.first()) }
    var notes by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf<TeamEntity?>(null) }
    var teamsExpanded by remember { mutableStateOf(false) }
    val reservationState by reservationViewModel.uiState.collectAsState()

    LaunchedEffect(userTeams) {
        selectedTeam = when {
            userTeams.isEmpty() -> null
            selectedTeam == null -> userTeams.first()
            userTeams.none { it.id == selectedTeam?.id } -> userTeams.first()
            else -> selectedTeam
        }
    }

    LaunchedEffect(reservationState.reservation) {
        reservationState.reservation?.let {
            reservationViewModel.clearStatus()
            navController.navigate("confirmation")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar cancha") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Selecciona una cancha", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
            val errorMessage = fieldState.error
            when {
                fieldState.isLoading -> {
                    CircularProgressIndicator(color = colorResource(R.color.neon_green))
                }

                errorMessage != null -> {
                    Text(errorMessage, color = colorResource(R.color.error_red))
                }

                fieldState.fields.isEmpty() -> {
                    Text("No hay canchas registradas", color = colorResource(R.color.text_medium_gray))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(fieldState.fields) { field ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .safeClickable { selectedField = field },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedField?.id == field.id) colorResource(R.color.surface_dark) else colorResource(R.color.background_black)
                                ),
                                border = if (selectedField?.id == field.id) BorderStroke(1.dp, colorResource(R.color.neon_green)) else null
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(field.nombre, color = colorResource(R.color.text_white), fontWeight = FontWeight.SemiBold)
                                    Text(field.direccion, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text("Fecha (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedContainerColor = colorResource(R.color.surface_dark)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Horario", color = colorResource(R.color.text_white), fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableSlots.forEach { slot ->
                        val isSelected = slot == selectedSlot
                        Button(
                            onClick = { selectedSlot = slot },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) colorResource(R.color.neon_green) else colorResource(R.color.surface_dark)
                            )
                        ) {
                            Text(slot, color = if (isSelected) colorResource(R.color.background_black) else colorResource(R.color.text_white))
                        }
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas adicionales") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedContainerColor = colorResource(R.color.surface_dark)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            Text("Selecciona tu equipo", color = colorResource(R.color.text_white), fontWeight = FontWeight.SemiBold)
            if (userTeams.isEmpty()) {
                Text(
                    text = "No tienes equipos creados. Crea uno antes de reservar.",
                    color = colorResource(R.color.text_medium_gray)
                )
            } else {
                ExposedDropdownMenuBox(
                    expanded = teamsExpanded,
                    onExpandedChange = { teamsExpanded = !teamsExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedTeam?.nombre.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Equipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teamsExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.surface_dark),
                            unfocusedContainerColor = colorResource(R.color.surface_dark)
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                    )
                    ExposedDropdownMenu(
                        expanded = teamsExpanded,
                        onDismissRequest = { teamsExpanded = false }
                    ) {
                        userTeams.forEach { team ->
                            DropdownMenuItem(
                                text = { Text(team.nombre) },
                                onClick = {
                                    selectedTeam = team
                                    teamsExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            reservationState.error?.let {
                Text(it, color = colorResource(R.color.error_red))
            }

            if (reservationState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    val field = selectedField ?: return@Button
                    val team = selectedTeam ?: return@Button
                    val request = CreateReservationRequestDto(
                        cancha = field.nombre,
                        fecha = selectedDate,
                        horaInicio = selectedSlot,
                        horaFin = addOneHour(selectedSlot),
                        equipoId = team.id,
                        notas = notes.ifBlank { null },
                        fieldId = field.id,
                        estado = "PENDIENTE"
                    )
                    reservationViewModel.createReservation(request)
                },
                enabled = selectedField != null && selectedTeam != null && !reservationState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
            ) {
                Text("Confirmar reserva", color = colorResource(R.color.background_black), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenCompose(navController: NavController) {
    val upcomingMatches = listOf(
        "Jueves 18:00" to "Cancha La Videna",
        "Viernes 20:00" to "Los Olivos Arena",
        "Domingo 09:00" to "Club del Centro"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda de partidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(upcomingMatches.size) { index ->
                val (schedule, place) = upcomingMatches[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(schedule, color = colorResource(R.color.neon_green), fontWeight = FontWeight.Bold)
                        Text(place, color = colorResource(R.color.text_white))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreenCompose(navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.estado.collectAsState()
    val usuario = authState.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("✅", fontSize = 48.sp)
            Text("Tu reserva está lista", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            usuario?.let {
                Text(
                    text = "Se registró a nombre de ${it.nombre}",
                    color = colorResource(R.color.text_medium_gray)
                )
            }
            Text("Te enviaremos una notificación con los detalles y un recordatorio 1 hora antes.", color = colorResource(R.color.text_medium_gray), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
            ) {
                Text("Volver al inicio", color = colorResource(R.color.background_black))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreenCompose(navController: NavController) {
    val mockThreads = listOf(
        "Equipo Fénix" to "¿Listos para mañana?",
        "Administrador Cancha" to "Reserva confirmada",
        "Carlos (DT)" to "Te falta un jugador?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensajes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockThreads.size) { index ->
                val (title, preview) = mockThreads[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .safeClickable { navController.navigate("chat") },
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(title, color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                        Text(preview, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenCompose(navController: NavController) {
    val messages = remember {
        mutableStateListOf(
            "Equipo Fénix: Llegamos 10 minutos antes",
            "Tú: Perfecto, nos vemos"
        )
    }
    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    val isOwn = message.startsWith("Tú:")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
                    ) {
                        Text(
                            text = message,
                            color = colorResource(R.color.background_black),
                            modifier = Modifier
                                .background(
                                    if (isOwn) colorResource(R.color.neon_green) else colorResource(R.color.surface_dark),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mensaje") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add("Tú: ${messageText.trim()}")
                            messageText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
                ) {
                    Text("Enviar", color = colorResource(R.color.background_black))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEquipoScreenCompose(
    navController: NavController,
    authViewModel: AuthViewModel,
    teamViewModel: TeamViewModel
) {
    val teamState by teamViewModel.uiState.collectAsState()
    val authState by authViewModel.estado.collectAsState()
    val currentUserId = authState.usuarioActual?.id
    var nombre by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("Intermedio") }
    var contacto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun clearForm() {
        nombre = ""
        ciudad = ""
        nivel = "Intermedio"
        contacto = ""
        descripcion = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear equipo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Completa la información de tu equipo para empezar a recibir retos.", color = colorResource(R.color.text_medium_gray))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.surface_dark),
                unfocusedContainerColor = colorResource(R.color.surface_dark)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del equipo") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            OutlinedTextField(
                value = nivel,
                onValueChange = { nivel = it },
                label = { Text("Nivel") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Contacto (teléfono o correo)") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = fieldColors,
                textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
            )

            errorMessage?.let {
                Text(it, color = colorResource(R.color.error_red))
            }

            teamState.error?.let {
                Text(it, color = colorResource(R.color.error_red))
            }

            if (currentUserId == null) {
                Text(
                    text = "Debes iniciar sesión para crear un equipo",
                    color = colorResource(R.color.text_medium_gray)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombre.isBlank()) {
                        errorMessage = "El nombre es obligatorio"
                    } else if (currentUserId == null) {
                        errorMessage = "Inicia sesión para crear un equipo"
                    } else {
                        val newTeam = TeamEntity(
                            id = UUID.randomUUID().toString(),
                            nombre = nombre,
                            ciudad = ciudad.ifEmpty { null },
                            nivel = nivel.ifEmpty { null },
                            contacto = contacto.ifEmpty { null },
                            descripcion = descripcion.ifEmpty { null },
                            userId = currentUserId
                        )
                        teamViewModel.insertTeam(newTeam)
                        clearForm()
                        errorMessage = null
                        navController.popBackStack()
                    }
                },
                enabled = !teamState.isLoading && currentUserId != null,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
            ) {
                Text("Guardar", color = colorResource(R.color.background_black), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// PANTALLA 8: MAPA DE CANCHAS CON GPS NATIVO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaCanchasScreenCompose(
    navController: NavController,
    fieldViewModel: FieldViewModel = viewModel(factory = FieldViewModelFactory(FieldRepository()))
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationTracker = remember(context.applicationContext) {
        LocationTracker(context.applicationContext)
    }
    val gpsCalculator = remember { GpsCalculator() }
    val fieldState = fieldViewModel.uiState

    val userLocationState = remember { mutableStateOf<android.location.Location?>(null) }
    val isRequestingLocation = remember { mutableStateOf(false) }
    val permissionGranted = remember { mutableStateOf(locationTracker.hasLocationPermission()) }
    val permissionError = remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionGranted.value = granted
        if (granted) {
            permissionError.value = null
            scope.launch { loadUserLocation(locationTracker, userLocationState, isRequestingLocation) }
        } else {
            permissionError.value = "Se necesita el permiso para mostrar tu ubicación en el mapa."
        }
    }

    LaunchedEffect(permissionGranted.value) {
        if (permissionGranted.value && userLocationState.value == null && !isRequestingLocation.value) {
            loadUserLocation(locationTracker, userLocationState, isRequestingLocation)
        }
    }

    LaunchedEffect(Unit) {
        fieldViewModel.fetchFields()
    }

    val originLat = userLocationState.value?.latitude ?: DEFAULT_LAT_LIMA
    val originLon = userLocationState.value?.longitude ?: DEFAULT_LON_LIMA

    val distancias = remember(originLat, originLon, fieldState.fields) {
        fieldState.fields.map { field ->
            val distancia = gpsCalculator.calcularDistanciaGPS(originLat, originLon, field.latitud, field.longitud)
            FieldWithDistance(field, distancia)
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
    }

    val userMarker = userLocationState.value?.let {
        OsmMarker(
            latitude = it.latitude,
            longitude = it.longitude,
            title = "Tu ubicación",
            description = "Actualizada con GPS nativo"
        )
    }
    val canchaMarkers = fieldState.fields.map {
        OsmMarker(latitude = it.latitud, longitude = it.longitud, title = it.nombre, description = it.direccion)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Canchas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.surface_dark))
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        OpenStreetMapView(
                            userMarker = userMarker,
                            canchaMarkers = canchaMarkers,
                            modifier = Modifier.fillMaxSize(),
                            zoom = 12.0
                        )

                        when {
                            !permissionGranted.value -> {
                                BookingMapOverlay {
                                    Text(
                                        text = "Necesitamos tu permiso de ubicación para mostrar el mapa en tiempo real.",
                                        color = colorResource(R.color.text_white)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(onClick = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }) {
                                        Text("Conceder permiso")
                                    }
                                }
                            }

                            isRequestingLocation.value || fieldState.isLoading -> {
                                BookingMapOverlay {
                                    CircularProgressIndicator(color = colorResource(R.color.neon_green))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Sincronizando datos...", color = colorResource(R.color.text_white))
                                }
                            }

                            fieldState.error != null -> {
                                BookingMapOverlay {
                                    Text(fieldState.error, color = colorResource(R.color.error_red))
                                }
                            }

                            canchaMarkers.isEmpty() -> {
                                BookingMapOverlay {
                                    Text("Sin canchas registradas", color = colorResource(R.color.text_white))
                                }
                            }
                        }
                    }
                }
            }

            items(distancias, key = { it.field.id }) { info ->
                val estaCerca = (info.distanceKm ?: Double.MAX_VALUE) < 5.0
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .safeClickable { navController.navigate("booking") },
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    border = if (estaCerca) {
                        BorderStroke(2.dp, colorResource(R.color.neon_green))
                    } else null
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                info.field.nombre,
                                color = colorResource(R.color.neon_green),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            info.distanceKm?.let { km ->
                                Text(
                                    text = formatDistance(km),
                                    color = if (estaCerca) colorResource(R.color.neon_green) else colorResource(R.color.text_medium_gray),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            info.field.direccion,
                            color = colorResource(R.color.text_medium_gray),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
                    border = BorderStroke(1.dp, colorResource(R.color.neon_green))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "🗺️ OpenStreetMap + GPS del dispositivo",
                            color = colorResource(R.color.neon_green),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            "Renderizamos los mapas con OpenStreetMap (osmdroid) y calculamos distancias con la API de ubicación de Android para mantener todo nativo.",
                            color = colorResource(R.color.text_medium_gray),
                            fontSize = 11.sp
                        )
                        permissionError.value?.let { error ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BookingMapOverlay(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black).copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(colorResource(R.color.surface_dark).copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

private fun formatDistance(distanceKm: Double): String {
    return if (distanceKm < 1) {
        val meters = (distanceKm * 1000).roundToInt()
        "$meters m"
    } else {
        String.format("%.1f km", distanceKm)
    }
}

private suspend fun loadUserLocation(
    locationTracker: LocationTracker,
    userLocationState: MutableState<Location?>,
    isRequestingLocation: MutableState<Boolean>
) {
    try {
        isRequestingLocation.value = true
        val location = locationTracker.getCurrentLocation()
        userLocationState.value = location
    } finally {
        isRequestingLocation.value = false
    }
}

private const val DEFAULT_LAT_LIMA = -12.0464
private const val DEFAULT_LON_LIMA = -77.0428

private data class FieldWithDistance(
    val field: Field,
    val distanceKm: Double?
)


// PANTALLA 9: PERFIL DE USUARIO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreenCompose(navController: NavController, authViewModel: AuthViewModel? = null) {
    val authState = authViewModel?.estado?.collectAsState()?.value
    val usuarioActual = authState?.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Avatar y nombre
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.surface_dark), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(colorResource(R.color.neon_green), shape = RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 40.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(usuarioActual?.nombre ?: "Nombre", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(usuarioActual?.correo ?: "correo@email.com", color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                }
            }

            item {
                // Estadísticas
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Estadísticas", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("Partidos", "12")
                        StatCard("Goles", "24")
                        StatCard("Rating", "4.8")
                    }
                }
            }

            item {
                // Botones
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
                ) {
                    Text("Editar Perfil", color = colorResource(R.color.background_black))
                }
            }
        }
    }
}

@Composable
fun RowScope.StatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(colorResource(R.color.surface_dark), RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = colorResource(R.color.neon_green), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(title, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
    }
}

// PANTALLA 10: NOTIFICACIONES
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreenCompose(navController: NavController) {
    val mockNotificaciones = listOf(
        "Te han desafiado - Equipo Rojo" to "Hace 5 min",
        "Se aceptó tu solicitud - Equipo Azul" to "Hace 1 hora",
        "Nueva cancha disponible - Centro" to "Hace 2 horas",
        "Tu perfil fue visto - 3 usuarios" to "Hace 3 horas",
        "Recuerdo: Partido mañana 19:00" to "Hace 5 horas"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mockNotificaciones.size) { index ->
                NotificationItem(
                    titulo = mockNotificaciones[index].first,
                    tiempo = mockNotificaciones[index].second
                )
            }
        }
    }
}

@Composable
fun NotificationItem(titulo: String, tiempo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(colorResource(R.color.neon_green), shape = RoundedCornerShape(50))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                Text(tiempo, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
            }
        }
    }
}

private fun Modifier.safeClickable(onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interactionSource,
        indication = rememberRipple(),
        onClick = onClick
    )
}

private fun currentIsoDate(): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
    return formatter.format(java.util.Date())
}

private fun addOneHour(hour: String): String {
    val parts = hour.split(":")
    if (parts.size != 2) return hour
    val startHour = parts[0].toIntOrNull() ?: return hour
    val minutes = parts[1]
    val resultHour = (startHour + 1).coerceAtMost(23)
    return String.format(java.util.Locale.US, "%02d:%s", resultHour, minutes)
}
