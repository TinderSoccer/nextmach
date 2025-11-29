package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nextmatch.app.data.repository.TeamRepository // Import TeamRepository

class TeamViewModelFactory(
    private val teamRepository: TeamRepository // Now takes TeamRepository directly
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeamViewModel(teamRepository) as T // Pass the provided repository
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
