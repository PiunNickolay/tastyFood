package ru.nick.tastytips.data.model

import com.squareup.moshi.Json

data class RandomRecipesResponse(
    @Json(name = "recipes") val recipes: List<RecipeDto>
)

data class RecipeDto(
    val id: Int,
    val title: String,
    val readyInMinutes: Int?,
    val image: String?
)


