package com.nextmatch.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nextmatch.app.data.repository.AuthRepository
import com.nextmatch.app.ui.screen.*
import com.nextmatch.app.viewmodel.AuthViewModel
import com.nextmatch.app.viewmodel.AuthViewModelFactory
import com.nextmatch.app.viewmodel.ContactoViewModel
import com.nextmatch.app.viewmodel.UsuarioViewModel

import com.nextmatch.app.data.repository.FieldRepository
import com.nextmatch.app.data.repository.ReservationRepository
import com.nextmatch.app.viewmodel.FieldViewModel
import com.nextmatch.app.viewmodel.FieldViewModelFactory
import com.nextmatch.app.viewmodel.ReservationViewModel
import com.nextmatch.app.viewmodel.ReservationViewModelFactory
import androidx.navigation.NavType // New import
import androidx.navigation.navArgument // New import
import com.nextmatch.app.data.remote.BackendApiModule // Import BackendApiModule
import com.nextmatch.app.data.remote.PlayerApiService // Import PlayerApiService
import com.nextmatch.app.data.remote.TeamApiService // Import TeamApiService
import com.nextmatch.app.data.repository.PlayerRepository // Import PlayerRepository
import com.nextmatch.app.data.repository.TeamRepository // Import TeamRepository
import com.nextmatch.app.viewmodel.PlayerViewModel // Import PlayerViewModel
import com.nextmatch.app.viewmodel.PlayerViewModelFactory // Import PlayerViewModelFactory
import com.nextmatch.app.viewmodel.TeamViewModel // Import TeamViewModel
import com.nextmatch.app.viewmodel.TeamViewModelFactory // Import TeamViewModelFactory


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels compartidos
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val contactoViewModel: ContactoViewModel = viewModel()

    // AuthViewModel con inyección de dependencias
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthRepository())
    )

    // Transiciones suaves reutilizables
    val slideInRight = slideInHorizontally(initialOffsetX = { 300 }) + fadeIn()
    val slideOutLeft = slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut()


    val fieldViewModel: FieldViewModel = viewModel(
        factory = FieldViewModelFactory(FieldRepository())
    )
    val reservationViewModel: ReservationViewModel = viewModel(
        factory = ReservationViewModelFactory(ReservationRepository())
    )

    val teamApiService = remember { BackendApiModule.create(TeamApiService::class.java) }
    val teamRepository = remember { TeamRepository(teamApiService) }
    val teamViewModel: TeamViewModel = viewModel(
        factory = TeamViewModelFactory(teamRepository)
    )

    val playerApiService = remember { BackendApiModule.create(PlayerApiService::class.java) }
    val playerRepository = remember { PlayerRepository(playerApiService) }
    val playerViewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModelFactory(playerRepository)
    )

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
            exitTransition = { fadeOut() }
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
            TeamsSelectionScreen(navController, teamViewModel)
        }
        composable(
            route = "my_teams", // New route for MyTeamsScreen
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MyTeamsScreenCompose(navController, authViewModel, teamViewModel)
        }
        composable(
            route = "match_found",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MatchFoundScreen(navController, reservationViewModel)
        }
        composable(
            route = "team_profile/{teamId}", // Modified route
            arguments = listOf(navArgument("teamId") { type = NavType.StringType }), // Add argument
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId")
            TeamProfileScreenCompose(
                navController = navController,
                teamId = teamId,
                authViewModel = authViewModel,
                teamViewModel = teamViewModel,
                playerViewModel = playerViewModel
            ) // Pass teamId and ViewModels
        }

        // Reserva de Cancha
        composable(
            route = "booking",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            BookingScreenCompose(
                navController,
                fieldViewModel,
                reservationViewModel,
                teamViewModel,
                authViewModel
            )
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
            exitTransition = { fadeOut() }
        ) {
            ConfirmationScreenCompose(navController, authViewModel)
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
            CrearEquipoScreenCompose(navController, authViewModel, teamViewModel)
        }
        composable(
            route = "mapa_canchas",
            enterTransition = { slideInRight },
            exitTransition = { slideOutLeft }
        ) {
            MapaCanchasScreenCompose(navController, fieldViewModel)
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
