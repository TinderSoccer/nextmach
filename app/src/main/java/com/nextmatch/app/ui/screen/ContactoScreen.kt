package com.nextmatch.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nextmatch.app.viewmodel.ContactoViewModel

@Composable
fun ContactoScreen(
    navController: NavController,
    viewModel: ContactoViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text(
            text = "Formulario de Contacto",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo nombre
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text(text = "Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo email
        OutlinedTextField(
            value = estado.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text(text = "Email") },
            isError = estado.errores.email != null,
            supportingText = {
                estado.errores.email?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo teléfono
        OutlinedTextField(
            value = estado.telefono,
            onValueChange = viewModel::onTelefonoChange,
            label = { Text(text = "Teléfono") },
            isError = estado.errores.telefono != null,
            supportingText = {
                estado.errores.telefono?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo mensaje
        OutlinedTextField(
            value = estado.mensaje,
            onValueChange = viewModel::onMensajeChange,
            label = { Text(text = "Mensaje") },
            isError = estado.errores.mensaje != null,
            supportingText = {
                estado.errores.mensaje?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // Botón: enviar
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    navController.navigate(route = "nueva")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Enviar")
        }

        // Botón para volver
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }
}
