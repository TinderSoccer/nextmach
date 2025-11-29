package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nextmatch.app.data.repository.FieldRepository

class FieldViewModelFactory(private val repository: FieldRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}