package ru.nick.tastytips.data.model

import com.squareup.moshi.Json

data class ComplexSearchResponse(
    @Json(name = "results") val results: List<RecipeSearchDto>
)

data class RecipeSearchDto(
    val id: Int,
    val title: String,
    val image: String?
)