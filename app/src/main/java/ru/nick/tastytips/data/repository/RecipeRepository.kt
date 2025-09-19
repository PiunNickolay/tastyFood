package ru.nick.tastytips.data.repository

import android.util.Log
import ru.nick.tastytips.R
import ru.nick.tastytips.data.remote.SpoonacularApi
import ru.nick.tastytips.domain.model.Category
import ru.nick.tastytips.domain.model.Recipe
import ru.nick.tastytips.domain.model.RecipeDetail
import ru.nick.tastytips.data.mappers.*

class RecipeRepository(private val api: SpoonacularApi) {
    suspend fun getRandomRecipes(): List<Recipe> {
        return try {
            val resp = api.getRandomRecipes()
            Log.d("REPO", "getRandomRecipes: recipes = ${resp.recipes.size}")
            val mapped = resp.recipes.map { it.toDomain() }
            Log.d("REPO", "getRandomRecipes mapped = ${mapped.size}")
            mapped
        } catch (e: Exception) {
            Log.e("REPO", "getRandomRecipes failed", e)
            emptyList()
        }
    }

    suspend fun getRecipesByCategory(categoryId: String): List<Recipe> {
        return try {
            val resp = api.getRecipesByCategory(type = categoryId) 
            Log.d("REPO", "getRecipesByCategory raw results size = ${resp.results.size}")
            val mapped = resp.results.map { dto ->
                Recipe(id = dto.id, title = dto.title, subtitle = "Category: $categoryId", imageUrl = dto.image)
            }
            Log.d("REPO", "getRecipesByCategory mapped size = ${mapped.size}")
            mapped
        } catch (e: Exception) {
            Log.e("REPO", "getRecipesByCategory failed", e)
            emptyList()
        }
    }

    suspend fun getRecipeDetail(id: Int): RecipeDetail {
        return api.getRecipeInformation(id).toDomain()
    }

    fun getCategories() : List<Category> {
        return listOf(
            Category("side dish", "Side Dish", R.drawable.ic_side_dish),
            Category("dessert", "Dessert", R.drawable.ic_food_24),
            Category("main course", "Main Course", R.drawable.ic_main_course),
            Category("drink", "Drinks", R.drawable.ic_drink)
        )
    }
}
