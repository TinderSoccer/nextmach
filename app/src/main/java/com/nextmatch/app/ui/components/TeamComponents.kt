package com.nextmatch.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nextmatch.app.R
import com.nextmatch.app.data.entities.TeamEntity
import java.util.UUID

@Composable
fun TeamListItem(team: TeamEntity, selected: Boolean, onSelect: () -> Unit) {
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
                Text(team.nombre, color = colorResource(R.color.text_white), fontWeight = FontWeight.SemiBold)
                team.ciudad?.let {
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
                    Icon(
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

private fun Modifier.safeClickable(onClick: () -> Unit): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interaction,
        indication = rememberRipple(),
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDialog(
    team: TeamEntity?, // null for add, TeamEntity for edit
    onDismiss: () -> Unit,
    onConfirm: (TeamEntity) -> Unit
) {
    var id by remember { mutableStateOf(team?.id ?: UUID.randomUUID().toString()) }
    var nombre by remember { mutableStateOf(team?.nombre ?: "") }
    var ciudad by remember { mutableStateOf(team?.ciudad ?: "") }
    var nivel by remember { mutableStateOf(team?.nivel ?: "Principiante") }
    var contacto by remember { mutableStateOf(team?.contacto ?: "") }
    var descripcion by remember { mutableStateOf(team?.descripcion ?: "") }
    // userId is handled by the calling screen/ViewModel for security. Not directly editable here.

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (team == null) "Agregar Nuevo Equipo" else "Editar Equipo", color = colorResource(R.color.text_white)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad") },
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
                    value = contacto,
                    onValueChange = { contacto = it },
                    label = { Text("Contacto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.surface_dark),
                        unfocusedContainerColor = colorResource(R.color.surface_dark)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = colorResource(R.color.text_white))
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        TeamEntity(
                            id = id,
                            nombre = nombre,
                            ciudad = ciudad.ifEmpty { null },
                            nivel = nivel.ifEmpty { null },
                            contacto = contacto.ifEmpty { null },
                            descripcion = descripcion.ifEmpty { null },
                            userId = team?.userId // Preserve existing userId or leave null for new team
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
