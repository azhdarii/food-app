package com.azhdar.myapplication5.presentation.foodlist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.azhdar.myapplication5.data.model.remote.Food
import com.azhdar.myapplication5.R
import com.bumptech.glide.Glide


class FoodAdapter(
    private var foods: List<Food?>,
    private val onItemClick: (Food?) -> Unit

) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodImage:ImageView=itemView.findViewById(R.id.itemFoodsImg)
        private val foodTitle:TextView=itemView.findViewById(R.id.itemFoodsTitle)
        private val foodCategory:TextView=itemView.findViewById(R.id.itemFoodsCategory)
        private val foodArea:TextView=itemView.findViewById(R.id.itemFoodsArea)
        fun bindData(position: Int) {

            Glide.with(itemView.context)
                .load(foods[position]?.strMealThumb)
                .into(foodImage)

            foodTitle.text=foods[position]?.strMeal
            foodCategory.text=foods[position]?.strCategory
            foodArea.text=foods[position]?.strArea

            itemView.setOnClickListener { onItemClick(foods[position]) }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foods, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = foods.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
        holder.itemView.setOnClickListener { v ->
            // Handle click here
            onItemClick(foods[position])
            Toast.makeText(v.context, "Clicked: " + holder.itemView.findViewById<TextView>(R.id.itemFoodsTitle).text, Toast.LENGTH_SHORT).show()
        }


    }
    fun updateList(newList: List<Food?>) {
        foods = newList
        notifyDataSetChanged()
    }

}