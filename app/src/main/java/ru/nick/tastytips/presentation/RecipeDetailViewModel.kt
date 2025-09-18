package ru.nick.tastytips.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.domain.model.RecipeDetail

class RecipeDetailViewModel (
    private val repository: RecipeRepository,
    private val recipeId: Int
): ViewModel() {
    private val _recipe = MutableLiveData<RecipeDetail>()
    val recipe: LiveData<RecipeDetail> = _recipe

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadRecipeDetail(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repository.getRecipeDetail(id)
                _recipe.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}