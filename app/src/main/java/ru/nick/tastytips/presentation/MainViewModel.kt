package ru.nick.tastytips.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nick.tastytips.BuildConfig
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.domain.model.Category
import ru.nick.tastytips.domain.model.Recipe

class MainViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipe: LiveData<List<Recipe>> = _recipes

    private val _recipesLoading = MutableLiveData<Boolean>()
    val recipesLoading: LiveData<Boolean> = _recipesLoading

    private val _recipesError = MutableLiveData<String?>()
    val recipesError: LiveData<String?> = _recipesError

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _categoriesLoading = MutableLiveData<Boolean>()
    val categoriesLoading: LiveData<Boolean> = _categoriesLoading

    private val _categoriesError = MutableLiveData<String?>()
    val categoriesError: LiveData<String?> = _categoriesError

    fun loadCategories() {
        Log.d("API_KEY_TEST", "Key from BuildConfig: ${BuildConfig.API_KEY}")
        viewModelScope.launch {
            _categoriesLoading.value = true
            _categoriesError.value = null
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getCategories()
                }
                _categories.value = result
                Log.d("VM", "loadCategories got ${result.size}")
            } catch (e: Exception) {
                _categoriesError.value = e.message
                Log.e("VM", "loadCategories error", e)
            } finally {
                _categoriesLoading.value = false
            }
        }
    }

    fun loadRecipesByCategory(category: Category) {
        viewModelScope.launch {
            _recipesLoading.value = true
            _recipesError.value = null
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getRecipesByCategory(category.id)
                }
                _recipes.value = result
                Log.d("VM", "loadRecipesByCategory got ${result.size}")
            } catch (e: Exception) {
                _recipesError.value = e.message
                Log.e("VM", "loadRecipesByCategory error", e)
            } finally {
                _recipesLoading.value = false
            }
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _recipesLoading.value = true
            _recipesError.value = null
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getRandomRecipes()
                }
                Log.d("VM", "recipes: ${result.joinToString { it.title }}")
                _recipes.value = result
                Log.d("VM", "loadRecipes got ${result.size}")
            } catch (e: Exception) {
                _recipesError.value = e.message
                Log.e("VM", "loadRecipes error", e)
            } finally {
                _recipesLoading.value = false
            }
        }
    }
}
