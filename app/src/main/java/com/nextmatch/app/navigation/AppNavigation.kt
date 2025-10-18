package com.nextmatch.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nextmatch.app.ui.screen.*
import com.nextmatch.app.viewmodel.ContactoViewModel
import com.nextmatch.app.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels compartidos
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val contactoViewModel: ContactoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla Principal
        composable(route = "home") {
            HomeScreen(navController)
        }

        // Autenticación
        composable(route = "login") {
            LoginScreenCompose(navController)
        }
        composable(route = "registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable(route = "resumen") {
            ResumenScreen(navController, usuarioViewModel)
        }

        // Búsqueda y Equipos
        composable(route = "matchmaking") {
            MatchmakingScreenCompose(navController)
        }
        composable(route = "teams") {
            TeamsListScreenCompose(navController)
        }
        composable(route = "team_profile") {
            TeamProfileScreenCompose(navController)
        }

        // Reserva de Cancha
        composable(route = "booking") {
            BookingScreenCompose(navController)
        }
        composable(route = "calendar") {
            CalendarScreenCompose(navController)
        }
        composable(route = "confirmation") {
            ConfirmationScreenCompose(navController)
        }

        // Mensajes
        composable(route = "messages") {
            MessagesScreenCompose(navController)
        }
        composable(route = "chat") {
            ChatScreenCompose(navController)
        }

        // Pantallas Adicionales
        composable(route = "crear_equipo") {
            CrearEquipoScreenCompose(navController)
        }
        composable(route = "mapa_canchas") {
            MapaCanchasScreenCompose(navController)
        }
        composable(route = "perfil_usuario") {
            PerfilUsuarioScreenCompose(navController)
        }
        composable(route = "notificaciones") {
            NotificacionesScreenCompose(navController)
        }

        // Formularios Originales
        composable(route = "nueva") {
            NuevaPantallaScreen(navController)
        }
        composable(route = "contacto") {
            ContactoScreen(navController, contactoViewModel)
        }
    }
}
