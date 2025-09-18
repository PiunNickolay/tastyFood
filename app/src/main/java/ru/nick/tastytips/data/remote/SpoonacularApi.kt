package ru.nick.tastytips.data.remote

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nick.tastytips.BuildConfig
import ru.nick.tastytips.data.model.ComplexSearchResponse
import ru.nick.tastytips.data.model.RandomRecipesResponse
import ru.nick.tastytips.data.model.RecipeDetailDto

interface SpoonacularApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("number") number: Int = 10,
    ): RandomRecipesResponse

    @GET("recipes/complexSearch")
    suspend fun getRecipesByCategory (
        @Query ("type") type: String,
        @Query ("number") number: Int = 10,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): ComplexSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path ("id") id: Int,
        @Query ("apiKey") apiKey: String = BuildConfig.API_KEY
    ): RecipeDetailDto
}