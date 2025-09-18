package ru.nick.tastytips.data.model

data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String?,
    val summary: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val instructions: String?,
    val extendedIngredients: List<IngredientDto>?
)
