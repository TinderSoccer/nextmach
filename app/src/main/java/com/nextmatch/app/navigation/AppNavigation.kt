package com.nextmatch.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nextmatch.app.NextMatchApplication
import com.nextmatch.app.ui.screen.*
import com.nextmatch.app.viewmodel.AuthViewModel
import com.nextmatch.app.viewmodel.AuthViewModelFactory
import com.nextmatch.app.viewmodel.ContactoViewModel
import com.nextmatch.app.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels compartidos
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val contactoViewModel: ContactoViewModel = viewModel()

    // AuthViewModel con inyección de dependencias
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(NextMatchApplication.database.usuarioDao())
    )

    // Transiciones suaves reutilizables
    val slideInRight = slideInHorizontally(initialOffsetX = { 300 }) + fadeIn()
    val slideOutLeft = slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut()
    val slideInLeft = slideInHorizontally(initialOffsetX = { -300 }) + fadeIn()
    val slideOutRight = slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut()

    NavHost(
        navController = navController,
        startDestination = "landing",
        enterTransition = { slideInRight },
        exitTransition = { slideOutLeft }
    ) {
        // Pantalla de Bienvenida/Landing
        composable(
            route = "landing",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LandingScreen(navController)
        }

        // Pantalla Principal
        composable(
            route = "home",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            HomeScreen(navController)
        }

        // Autenticación
        composable(
            route = "login",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            LoginScreenCompose(navController, authViewModel)
        }
        composable(
            route = "registro",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            RegistroScreen(navController, usuarioViewModel, authViewModel)
        }
        composable(
            route = "resumen",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            ResumenScreen(navController, usuarioViewModel)
        }

        // Búsqueda y Equipos
        composable(
            route = "matchmaking",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MatchmakingScreenNew(navController)
        }
        composable(
            route = "teams",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            TeamsSelectionScreen(navController)
        }
        composable(
            route = "match_found",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MatchFoundScreen(navController)
        }
        composable(
            route = "team_profile",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            TeamProfileScreenCompose(navController)
        }

        // Reserva de Cancha
        composable(
            route = "booking",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            BookingScreenCompose(navController)
        }
        composable(
            route = "calendar",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            CalendarScreenCompose(navController)
        }
        composable(
            route = "confirmation",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            ConfirmationScreenCompose(navController)
        }

        // Mensajes
        composable(
            route = "messages",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MessagesScreenCompose(navController)
        }
        composable(
            route = "chat",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            ChatScreenCompose(navController)
        }

        // Pantallas Adicionales
        composable(
            route = "crear_equipo",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            CrearEquipoScreenCompose(navController)
        }
        composable(
            route = "mapa_canchas",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MapaCanchasScreenCompose(navController)
        }
        composable(
            route = "perfil_usuario",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            PerfilUsuarioScreenCompose(navController, authViewModel)
        }
        composable(
            route = "notificaciones",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            NotificacionesScreenCompose(navController)
        }

        // Formularios Originales
        composable(
            route = "nueva",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            NuevaPantallaScreen(navController)
        }
        composable(
            route = "contacto",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            ContactoScreen(navController, contactoViewModel)
        }
    }
}
