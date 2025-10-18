package com.nextmatch.app.ui.screen

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .verticalScroll(rememberScrollState())
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text(
            text = "Registrar Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            color = colorResource(R.color.text_white),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo nombre
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text(text = "Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let {
                    Text(text = it, color = colorResource(R.color.error_red))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.text_white),
                unfocusedTextColor = colorResource(R.color.text_white),
                focusedBorderColor = colorResource(R.color.neon_green),
                unfocusedBorderColor = colorResource(R.color.surface_dark),
                focusedLabelColor = colorResource(R.color.neon_green),
                unfocusedLabelColor = colorResource(R.color.text_white)
            )
        )

        // Campo correo
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text(text = "Correo electrónico") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let {
                    Text(text = it, color = colorResource(R.color.error_red))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.text_white),
                unfocusedTextColor = colorResource(R.color.text_white),
                focusedBorderColor = colorResource(R.color.neon_green),
                unfocusedBorderColor = colorResource(R.color.surface_dark),
                focusedLabelColor = colorResource(R.color.neon_green),
                unfocusedLabelColor = colorResource(R.color.text_white)
            )
        )

        // Campo clave
        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = estado.errores.clave != null,
            supportingText = {
                estado.errores.clave?.let {
                    Text(text = it, color = colorResource(R.color.error_red))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.text_white),
                unfocusedTextColor = colorResource(R.color.text_white),
                focusedBorderColor = colorResource(R.color.neon_green),
                unfocusedBorderColor = colorResource(R.color.surface_dark),
                focusedLabelColor = colorResource(R.color.neon_green),
                unfocusedLabelColor = colorResource(R.color.text_white)
            )
        )

        // Campo dirección
        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = { Text(text = "Dirección") },
            isError = estado.errores.direccion != null,
            supportingText = {
                estado.errores.direccion?.let {
                    Text(text = it, color = colorResource(R.color.error_red))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.text_white),
                unfocusedTextColor = colorResource(R.color.text_white),
                focusedBorderColor = colorResource(R.color.neon_green),
                unfocusedBorderColor = colorResource(R.color.surface_dark),
                focusedLabelColor = colorResource(R.color.neon_green),
                unfocusedLabelColor = colorResource(R.color.text_white)
            )
        )

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

        // Botón: enviar
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    navController.navigate(route = "resumen")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Registrar", color = colorResource(R.color.background_black), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
