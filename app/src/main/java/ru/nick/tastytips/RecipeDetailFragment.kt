package ru.nick.tastytips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.nick.tastytips.data.remote.SpoonacularApi
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.databinding.FragmentRecipeDetailBinding
import ru.nick.tastytips.presentation.IngredientsAdapter
import ru.nick.tastytips.presentation.RecipeDetailViewModel

class RecipeDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val api = retrofit.create(SpoonacularApi::class.java)
        val repository = RecipeRepository(api)

        val recipeId = requireArguments().getInt("recipeId")

        val viewModel = ViewModelProvider(
            this,
            RecipeDetailViewModelFactory(repository, recipeId)
        )[RecipeDetailViewModel::class.java]

        val adapter = IngredientsAdapter()
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.ingredientsRecycler.adapter = adapter

        viewModel.recipe.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                binding.recipeTitle.text = it.title
                binding.recipeServings.text = "${it.servings} Persons"
                binding.recipeTime.text = "${it.readyInMinutes} Minutes"

                Glide.with(binding.recipeImage.context)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.ic_placeholder_24)
                    .into(binding.recipeImage)
                adapter.submitList(it.ingredients)
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, "Error: $it", Snackbar.LENGTH_LONG).show()
            }
        }
        viewModel.loadRecipeDetail(recipeId)
        return binding.root
    }
}