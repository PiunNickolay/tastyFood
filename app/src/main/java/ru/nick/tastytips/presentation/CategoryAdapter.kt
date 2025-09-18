package ru.nick.tastytips.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nick.tastytips.R
import ru.nick.tastytips.databinding.ItemCategoryBinding
import ru.nick.tastytips.domain.model.Category

typealias OnClickListenerCategory = (Category) -> Unit

class CategoryAdapter(
    private val onClick: OnClickListenerCategory
) : ListAdapter<Category, CategoryViewHolder>(CategoriesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

}

class CategoryViewHolder(
    private val binding: ItemCategoryBinding,
    private val onClick: OnClickListenerCategory
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Category) {
        binding.apply {
            categoryName.text = item.title
            val res = item.imagesRes ?: R.drawable.ic_food_24
            Glide.with(categoryIcon)
                .load(res)
                .circleCrop()
                .into(categoryIcon)
            root.setOnClickListener {
                Log.d("CAT", "clicked ${item.id}")
                onClick(item)
            }
        }
    }
}

class CategoriesDiffCallback : DiffUtil.ItemCallback<Category>(

) {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem == newItem
}