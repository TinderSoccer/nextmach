package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nextmatch.app.data.repository.PlayerRepository // Import PlayerRepository

class PlayerViewModelFactory(
    private val playerRepository: PlayerRepository // Now takes PlayerRepository directly
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(playerRepository) as T // Pass the provided repository
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
