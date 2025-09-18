package ru.nick.tastytips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.presentation.RecipeDetailViewModel

class RecipeDetailViewModelFactory(
    private val repository: RecipeRepository,
    private val recipeId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            return RecipeDetailViewModel(repository, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}