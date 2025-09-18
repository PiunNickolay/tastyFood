package ru.nick.tastytips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.presentation.MainViewModel

class MainViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}