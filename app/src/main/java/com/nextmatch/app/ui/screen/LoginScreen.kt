@file:OptIn(ExperimentalMaterial3Api::class)

package com.nextmatch.app.ui

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nextmatch.app.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color

/**
 * LoginScreen simple e interactivo.
 *
 * onLoginSuccess: lambda que se ejecuta cuando el login "falla" la validación.
 * (En el futuro reemplazarás la validación local por Firebase Auth)
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    // Estados locales para campos y mensajes
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Layout principal
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // toma el fondo del tema
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo (reutiliza tu drawable/logo.png)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo NextMatch",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Título
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPassword) PasswordVisualTransformation() else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "Ocultar" else "Mostrar")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mensaje de error (si existe)
            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFFF6B6B), // rojo suave
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón principal de login
            Button(
                onClick = {
                    // Validación simple local (para aprender)
                    when {
                        email.isBlank() || password.isBlank() -> {
                            errorMessage = "Por favor completa correo y contraseña."
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Correo inválido."
                        }
                        password.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres."
                        }
                        else -> {
                            // Validación ok -> simulamos login exitoso
                            errorMessage = null
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,      // verde/neón si tu tema lo tiene
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones secundarios (Google y Invitado - por ahora mock)
            OutlinedButton(
                onClick = { /* TODO: integrar Google Sign-In */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar con Google")
            }

            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = { /* login como invitado */ onLoginSuccess() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar como invitado")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Enlace a registro (solo visual)
            TextButton(onClick = { /* TODO: navegar a RegisterScreen */ }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
