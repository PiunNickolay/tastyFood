package ru.nick.tastytips.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nick.tastytips.R
import ru.nick.tastytips.databinding.ItemIngredientBinding
import ru.nick.tastytips.domain.model.Ingredient


class IngredientsAdapter() : ListAdapter<Ingredient,IngredientsViewHolder>(IngredientsDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class IngredientsViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: Ingredient){
        binding.apply {
            ingredientName.text = item.name
            ingredientAmount.text = item.original
            Glide.with(binding.ingredientImage)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_food_24)
                .into(binding.ingredientImage)
        }
    }
}

class IngredientsDiffCallback: DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean = oldItem == newItem

}