package ru.nick.tastytips.data.mappers
import androidx.core.text.HtmlCompat
import ru.nick.tastytips.data.model.RecipeDetailDto
import ru.nick.tastytips.data.model.RecipeDto
import ru.nick.tastytips.domain.model.Ingredient
import ru.nick.tastytips.domain.model.Recipe
import ru.nick.tastytips.domain.model.RecipeDetail

fun RecipeDto.toDomain(): Recipe {
    val subtitle = "Time: ${readyInMinutes ?: "?"} min"
    return Recipe(
        id = id,
        title = title,
        subtitle = subtitle,
        imageUrl = image
    )
}

fun RecipeDetailDto.toDomain(): RecipeDetail {
    return RecipeDetail(
        id = id,
        title = title,
        imageUrl = image,
        summary = summary?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "",
        readyInMinutes = readyInMinutes ?: 0,
        servings = servings ?: 0,
        instructions = instructions ?: "",
        ingredients = extendedIngredients?.map { ing ->
            Ingredient(
                id = ing.id,
                name = ing.name,
                original = ing.original,
                imageUrl = ing.image?.let { "https://spoonacular.com/cdn/ingredients_100x100/$it" }
            )
        } ?: emptyList()
    )
}