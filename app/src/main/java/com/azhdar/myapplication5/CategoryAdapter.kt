package com.azhdar.myapplication5

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoriesAdapter(
    private var categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.itemCategoriesTxt)
        private val categoryImage: ImageView = itemView.findViewById(R.id.itemCategoriesImg)

        fun bind(category: Category) {
            categoryName.text = category.strCategory
            Glide.with(itemView.context)
                .load(category.strCategoryThumb)
                .into(categoryImage)


            itemView.setBackgroundResource(
                if (category.isSelected) R.drawable.bg_rounded_selcted
                else R.drawable.bg_rounded_white
            )

            itemView.setOnClickListener {
                val previousSelected = categories.indexOfFirst { it.isSelected }
                categories.forEach { it.isSelected = false }
               if(previousSelected!=adapterPosition){
                   category.isSelected = true

               }


                if (previousSelected >= 0) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(adapterPosition)

                onItemClick(category)


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])



    }

    override fun getItemCount() = categories.size

    // In your adapters
    fun updateList(newList: List<Category>) {
        categories = newList
        notifyDataSetChanged()
    }
}