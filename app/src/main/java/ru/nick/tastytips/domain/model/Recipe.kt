package ru.nick.tastytips.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val subtitle: String,
    val imageUrl: String?
)
