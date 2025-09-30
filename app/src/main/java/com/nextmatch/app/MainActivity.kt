package com.nextmatch.app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.nextmatch.app.ui.LoginScreen
import com.nextmatch.app.ui.theme.NextMatchTheme
import com.nextmatch.app.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextMatchTheme(
                {
                    // Estado simple en memoria: si el usuario está logueado o no
                    var isLoggedIn by remember { mutableStateOf(false) }

                    if (!isLoggedIn) {
                        // Mostramos el LoginScreen y pasamos la lambda que cambia el estado
                        LoginScreen(onLoginSuccess = {
                            isLoggedIn = true
                        })
                    } else {
                        // Usuario "logueado": mostramos la HomeScreen
                        HomeScreen()
                    }
                },
            )
        }
    }
}
