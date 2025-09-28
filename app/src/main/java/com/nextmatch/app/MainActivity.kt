package com.nextmatch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nextmatch.app.ui.HomeScreen
import com.nextmatch.app.ui.theme.NextMatchTheme
import com.nextmatch.app.ui.theme.NextMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextMatchTheme {
                HomeScreen()
            }
        }
    }
}
