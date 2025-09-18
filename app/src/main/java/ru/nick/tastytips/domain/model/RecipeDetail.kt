package ru.nick.tastytips.domain.model

data class RecipeDetail(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val summary: String,
    val readyInMinutes: Int,
    val servings: Int,
    val instructions: String,
    val ingredients: List<Ingredient>
)