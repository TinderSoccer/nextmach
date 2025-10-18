package com.nextmatch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nextmatch.app.navigation.AppNavigation
import com.nextmatch.app.ui.theme.NextMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextMatchTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
