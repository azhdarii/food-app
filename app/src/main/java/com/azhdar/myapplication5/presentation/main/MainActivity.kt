package com.azhdar.myapplication5.presentation.main
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhdar.myapplication5.presentation.categorylist.CategoriesAdapter
import com.azhdar.myapplication5.presentation.foodlist.FoodAdapter
import com.azhdar.myapplication5.presentation.detailfood.FoodDetailActivity
import com.azhdar.myapplication5.presentation.foodlist.MealViewModel
import com.azhdar.myapplication5.databinding.RecyclerViewBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: RecyclerViewBinding
    private lateinit var viewModel: MealViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var foodAdapter: FoodAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)





            binding = RecyclerViewBinding.inflate(layoutInflater)
            setContentView(binding.root)



            viewModel = ViewModelProvider(this).get(MealViewModel::class.java)

            // Initialize Adapters with empty lists
            categoriesAdapter = CategoriesAdapter(emptyList()) { category ->
                // Clear previous observers to avoid duplicate updates
                viewModel.mealsByCategory.removeObservers(this)
                viewModel.mealsBySearch.removeObservers(this)

                if (viewModel.currentCategory == category.strCategory) {
                    // Clicked the already selected category - deselect it
                    viewModel.currentCategory = null
                    viewModel.fetchMealBySearch("a") // Load some default search

                    viewModel.mealsBySearch.observe(this) { meals ->
                        foodAdapter.updateList(meals)
                    }
                } else {
                    // Select new category
                    viewModel.currentCategory = category.strCategory
                    viewModel.fetchMealsByCategory(category.strCategory)

                    viewModel.mealsByCategory.observe(this) { meals ->
                        foodAdapter.updateList(meals)
                    }
                }
            }
            foodAdapter = FoodAdapter(emptyList()) { meal ->
                // Handle meal click - could open detail activity
                val intent = Intent(this, FoodDetailActivity::class.java).apply {
                    putExtra("MEAL_ID", meal?.idMeal)
                    // Alternatively, you can pass the entire meal object if it's Parcelable
                }
                startActivity(intent)
            }

            binding.categoriesRecycler.apply {
                adapter = categoriesAdapter
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                // Add item decoration for spacing if needed
            }
            // Setup Foods RecyclerView (horizontal)
            binding.foodsRecycler.apply {
                adapter = foodAdapter
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                // Add item decoration for spacing if needed
            }

            // Observe categories LiveData
            viewModel.categories.observe(this) { categories ->
                categories?.let {
                    categoriesAdapter.updateList(it)
                }        }

            // Observe meals LiveData
            viewModel.mealsBySearch.observe(this) { meals ->
                meals?.let {
                    foodAdapter.updateList(it)
                }
            }

            // Load initial data
            viewModel.fetchCategories()
            viewModel.fetchMealBySearch("a")





            // rest of your code
        } catch (e: Exception) {
            Log.e("111",e.toString())
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }








    }
}