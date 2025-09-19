package ru.nick.tastytips.presentation

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nick.tastytips.R
import ru.nick.tastytips.databinding.ItemRecipeBinding
import ru.nick.tastytips.domain.model.Recipe


typealias OnClickListenerRecipes = (Recipe) -> Unit

class RecipesAdapter(private val onClick: OnClickListenerRecipes) :
    ListAdapter<Recipe, RecipesViewHolder>(RecipeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        Log.d("ADAPTER", "bind pos=$position id=${item.id}")
    }

}

class RecipesViewHolder(
    private val binding: ItemRecipeBinding,
    private val onClick: OnClickListenerRecipes
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Recipe) {
        binding.apply {
            recipeTitle.text = item.title.ifBlank { "NO TITLE" }
            recipeSubtitle.text = item.subtitle ?: "No subtitle"

            Glide.with(recipeImage)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_placeholder_24)
                .into(recipeImage)

            recipeTitle.text = "CARD: ${item.title}"

            root.setOnClickListener {
                Log.d("CAT", "clicked ${item.id}")
                onClick(item)
            }
        }
    }

}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean = oldItem == newItem


}
