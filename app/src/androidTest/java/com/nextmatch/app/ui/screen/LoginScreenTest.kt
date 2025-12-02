package com.nextmatch.app.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nextmatch.app.data.repository.AuthRepository
import com.nextmatch.app.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        composeRule.setContent {
            val context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            navController.graph = navController.createGraph(startDestination = "login") {
                composable("login") {}
                composable("home") {}
                composable("registro") {}
            }

            authViewModel = AuthViewModel(AuthRepository())

            LoginScreenCompose(navController = navController, authViewModel = authViewModel)
        }
    }

    @Test
    fun shouldShowLoginFormElements() {
        composeRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
        composeRule.onNodeWithText("Registrarse").assertIsDisplayed()
        composeRule.onNodeWithText("¿Olvidaste tu contraseña?").assertIsDisplayed()
    }

    @Test
    fun shouldShowValidationErrorsWhenFormInvalid() {
        composeRule.onNodeWithText("Iniciar Sesión").performClick()

        composeRule.onNodeWithText("Email inválido").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña mínimo 6 caracteres").assertIsDisplayed()
    }

    @Test
    fun shouldNavigateToRegisterWhenRegisterButtonClicked() {
        composeRule.onNodeWithText("Registrarse").performClick()
        composeRule.waitForIdle()

        assertEquals("registro", navController.currentDestination?.route)
    }
}
