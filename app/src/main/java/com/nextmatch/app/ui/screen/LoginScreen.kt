package com.nextmatch.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenCompose(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("test@test.com") }
    var password by remember { mutableStateOf("password123") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    val authState by authViewModel.estado.collectAsState()

    // Navegar al home cuando la autenticación sea exitosa
    LaunchedEffect(authState.isAutenticado) {
        if (authState.isAutenticado) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo de la app con animación de escala
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de NextMatch",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 24.dp)
                    .graphicsLayer(
                        scaleX = if (authState.isLoading) 0.95f else 1f,
                        scaleY = if (authState.isLoading) 0.95f else 1f
                    )
            )

            // Email Input con animación de borde
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = { Text("Correo Electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .graphicsLayer(
                        scaleX = if (isEmailFocused) 1.02f else 1f,
                        scaleY = if (isEmailFocused) 1.02f else 1f
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.surface_dark),
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedTextColor = colorResource(R.color.text_white),
                    focusedTextColor = colorResource(R.color.text_white),
                    focusedIndicatorColor = colorResource(R.color.neon_green),
                    unfocusedIndicatorColor = colorResource(R.color.surface_dark)
                ),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                isEmailFocused = when (interaction) {
                                    is androidx.compose.foundation.interaction.FocusInteraction.Focus -> true
                                    is androidx.compose.foundation.interaction.FocusInteraction.Unfocus -> false
                                    else -> isEmailFocused
                                }
                            }
                        }
                    }
            )

            // Error animado para email
            AnimatedVisibility(
                visible = emailError.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    emailError,
                    color = colorResource(R.color.error_red),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Password Input con animación de borde
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .graphicsLayer(
                        scaleX = if (isPasswordFocused) 1.02f else 1f,
                        scaleY = if (isPasswordFocused) 1.02f else 1f
                    ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.surface_dark),
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedTextColor = colorResource(R.color.text_white),
                    focusedTextColor = colorResource(R.color.text_white),
                    focusedIndicatorColor = colorResource(R.color.neon_green),
                    unfocusedIndicatorColor = colorResource(R.color.surface_dark)
                ),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                isPasswordFocused = when (interaction) {
                                    is androidx.compose.foundation.interaction.FocusInteraction.Focus -> true
                                    is androidx.compose.foundation.interaction.FocusInteraction.Unfocus -> false
                                    else -> isPasswordFocused
                                }
                            }
                        }
                    }
            )

            // Error animado para contraseña
            AnimatedVisibility(
                visible = passwordError.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    passwordError,
                    color = colorResource(R.color.error_red),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar error general si existe
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

            // Login Button con efectos visuales
            Button(
                onClick = {
                    var valid = true
                    if (email.isEmpty() || !email.contains("@")) {
                        emailError = "Email inválido"
                        valid = false
                    }
                    if (password.isEmpty() || password.length < 6) {
                        passwordError = "Contraseña mínimo 6 caracteres"
                        valid = false
                    }
                    if (valid) {
                        authViewModel.iniciarSesion(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .graphicsLayer(
                        scaleX = if (authState.isLoading) 0.98f else 1f,
                        scaleY = if (authState.isLoading) 0.98f else 1f
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp),
                enabled = !authState.isLoading
            ) {
                Text(
                    if (authState.isLoading) "Cargando..." else "Iniciar Sesión",
                    color = colorResource(R.color.background_black)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password
            TextButton(onClick = { /* TODO */ }) {
                Text("¿Olvidaste tu contraseña?", color = colorResource(R.color.neon_green))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = { navController.navigate("registro") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registrarse", color = colorResource(R.color.neon_green))
            }
        }
    }
}