package ru.nick.tastytips

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nick.tastytips.data.remote.SpoonacularApi
import ru.nick.tastytips.data.repository.RecipeRepository
import ru.nick.tastytips.databinding.FragmentMainBinding
import ru.nick.tastytips.presentation.CategoryAdapter
import ru.nick.tastytips.presentation.MainViewModel
import ru.nick.tastytips.presentation.RecipesAdapter
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val api = retrofit.create(SpoonacularApi::class.java)
        val repository = RecipeRepository(api)
        val viewModel : MainViewModel

        viewModel = ViewModelProvider(this, MainViewModelFactory(repository))
            .get(MainViewModel::class.java)

        val recipesAdapter = RecipesAdapter { recipe ->
            val bundle = Bundle().apply {
                putInt("recipeId", recipe.id)
            }
            findNavController().navigate(R.id.action_mainFragment_to_recipeDetailFragment, bundle)
        }
        val categoryAdapter = CategoryAdapter { category ->
            Log.d("UI", "category clicked: ${category.id}")
            viewModel.loadRecipesByCategory(category)
        }

        with(binding) {
            recipesRecycler.apply {
                adapter = recipesAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }

            recipesAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
                override fun onChanged(){ Log.d("UI", "adapter onChanged") }
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int){
                    Log.d("UI", "adapter inserted $itemCount at $positionStart")
                }
            })


            categoriesRecycler.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }

        with(viewModel) {
            categories.observe(viewLifecycleOwner) {list->
                categoryAdapter.submitList(list)
            }

            categoriesLoading.observe(viewLifecycleOwner) {isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            categoriesError.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    Snackbar.make(binding.root, "Error: $it", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") {
                            viewModel.loadRecipes()
                        }
                        .show()
                }
            }

            recipe.observe(viewLifecycleOwner) { list ->
//                recipesAdapter.submitList(list)
                Log.d("UI", "observer received ${list?.size} recipes")
                recipesAdapter.submitList(list)
            }

            recipesLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            recipesError.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    Snackbar.make(binding.root, "Error: $it", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") {
                            viewModel.loadRecipes()
                        }
                        .show()
                }
            }
            loadCategories()
            loadRecipes()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val api = retrofit.create(SpoonacularApi::class.java)
        lifecycleScope.launchWhenStarted {
            try {
                val resp = api.getRecipesByCategory(type = "dessert", number = 3)
                Log.d("DIRECT_API", "raw results size=${resp.results.size}")
                resp.results.forEach { Log.d("DIRECT_API", "item: ${it.id} ${it.title}") }
            } catch (e: Exception) {
                Log.e("DIRECT_API", "direct call failed", e)
            }
        }
    }

}