package com.nextmatch.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nextmatch.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenCompose(navController: NavController) {
    var email by remember { mutableStateOf("test@test.com") }
    var password by remember { mutableStateOf("password123") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

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
            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de NextMatch",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 24.dp)
            )

            // Email Input
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = { Text("Correo Electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.surface_dark),
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedTextColor = colorResource(R.color.text_white),
                    focusedTextColor = colorResource(R.color.text_white),
                    focusedIndicatorColor = colorResource(R.color.neon_green)
                )
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
            }

            // Password Input
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.surface_dark),
                    focusedContainerColor = colorResource(R.color.surface_dark),
                    unfocusedTextColor = colorResource(R.color.text_white),
                    focusedTextColor = colorResource(R.color.text_white),
                    focusedIndicatorColor = colorResource(R.color.neon_green)
                )
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = colorResource(R.color.error_red), style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
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
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Iniciar Sesión", color = colorResource(R.color.background_black))
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