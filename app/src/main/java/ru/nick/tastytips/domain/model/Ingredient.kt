package ru.nick.tastytips.domain.model

data class Ingredient(
    val id: Int,
    val name: String,
    val original: String,
    val imageUrl: String?
)
