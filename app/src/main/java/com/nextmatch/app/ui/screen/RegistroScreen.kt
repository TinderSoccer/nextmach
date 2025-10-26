package com.nextmatch.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.viewmodel.AuthViewModel
import com.nextmatch.app.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    authViewModel: AuthViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val authState by authViewModel.estado.collectAsState()

    // Navegar al home cuando el registro sea exitoso
    LaunchedEffect(authState.isAutenticado) {
        if (authState.isAutenticado) {
            navController.navigate("home") {
                popUpTo("registro") { inclusive = true }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .verticalScroll(rememberScrollState())
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        // Título con animación de entrada
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn()
        ) {
            Text(
                text = "Registrar Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Campo nombre
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text(text = "Nombre") },
                    isError = estado.errores.nombre != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = if (estado.errores.nombre != null) 0.98f else 1f,
                            scaleY = if (estado.errores.nombre != null) 0.98f else 1f
                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.text_white),
                        unfocusedTextColor = colorResource(R.color.text_white),
                        focusedBorderColor = colorResource(R.color.neon_green),
                        unfocusedBorderColor = colorResource(R.color.surface_dark),
                        focusedLabelColor = colorResource(R.color.neon_green),
                        unfocusedLabelColor = colorResource(R.color.text_white)
                    )
                )
                AnimatedVisibility(
                    visible = estado.errores.nombre != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    estado.errores.nombre?.let {
                        Text(text = it, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Campo correo
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text(text = "Correo electrónico") },
                    isError = estado.errores.correo != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = if (estado.errores.correo != null) 0.98f else 1f,
                            scaleY = if (estado.errores.correo != null) 0.98f else 1f
                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.text_white),
                        unfocusedTextColor = colorResource(R.color.text_white),
                        focusedBorderColor = colorResource(R.color.neon_green),
                        unfocusedBorderColor = colorResource(R.color.surface_dark),
                        focusedLabelColor = colorResource(R.color.neon_green),
                        unfocusedLabelColor = colorResource(R.color.text_white)
                    )
                )
                AnimatedVisibility(
                    visible = estado.errores.correo != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    estado.errores.correo?.let {
                        Text(text = it, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Campo clave
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = viewModel::onClaveChange,
                    label = { Text(text = "Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.clave != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = if (estado.errores.clave != null) 0.98f else 1f,
                            scaleY = if (estado.errores.clave != null) 0.98f else 1f
                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.text_white),
                        unfocusedTextColor = colorResource(R.color.text_white),
                        focusedBorderColor = colorResource(R.color.neon_green),
                        unfocusedBorderColor = colorResource(R.color.surface_dark),
                        focusedLabelColor = colorResource(R.color.neon_green),
                        unfocusedLabelColor = colorResource(R.color.text_white)
                    )
                )
                AnimatedVisibility(
                    visible = estado.errores.clave != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    estado.errores.clave?.let {
                        Text(text = it, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Campo dirección
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = estado.direccion,
                    onValueChange = viewModel::onDireccionChange,
                    label = { Text(text = "Dirección") },
                    isError = estado.errores.direccion != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = if (estado.errores.direccion != null) 0.98f else 1f,
                            scaleY = if (estado.errores.direccion != null) 0.98f else 1f
                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.text_white),
                        unfocusedTextColor = colorResource(R.color.text_white),
                        focusedBorderColor = colorResource(R.color.neon_green),
                        unfocusedBorderColor = colorResource(R.color.surface_dark),
                        focusedLabelColor = colorResource(R.color.neon_green),
                        unfocusedLabelColor = colorResource(R.color.text_white)
                    )
                )
                AnimatedVisibility(
                    visible = estado.errores.direccion != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    estado.errores.direccion?.let {
                        Text(text = it, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Checkbox: aceptar términos
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange
            )
            Spacer(Modifier.width(width = 8.dp))
            Text(text = "Acepto los términos y condiciones", color = colorResource(R.color.text_white))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar error si existe
        AnimatedVisibility(
            visible = authState.error != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                authState.error ?: "",
                color = colorResource(R.color.error_red),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botón: enviar
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    authViewModel.registrarUsuario(
                        nombre = estado.nombre,
                        correo = estado.correo,
                        clave = estado.clave,
                        direccion = estado.direccion
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !authState.isLoading
        ) {
            Text(
                text = if (authState.isLoading) "Registrando..." else "Registrar",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Botón: volver
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.surface_dark)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Volver", color = colorResource(R.color.neon_green))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
