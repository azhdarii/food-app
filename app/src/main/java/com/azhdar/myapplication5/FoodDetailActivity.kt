package com.azhdar.myapplication5

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.azhdar.myapplication5.Food
import com.azhdar.myapplication5.MealViewModel
import com.azhdar.myapplication5.databinding.ActivityFoodDetailBinding
import com.bumptech.glide.Glide

class FoodDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var  viewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MealViewModel::class.java)



        val mealId = intent.getStringExtra("MEAL_ID") ?: run {
            finish()
            return
        }

        viewModel.fetchMealDetails(mealId)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.selectedMeal.observe(this) { meal ->
            meal?.let { displayMealDetails(it) }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Show/hide loading indicator
        }

        viewModel.error.observe(this) { error ->
            error?.let { showError(it) }
        }
    }

    private fun displayMealDetails(meal: Food) {
        // Load image
        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.foodImg)

        // Set basic info
        binding.foodTitle.text = meal.strMeal
        binding.foodCategoryTxt.text = meal.strCategory
        binding.foodAreaTxt.text = meal.strArea
        binding.foodDescription.text = meal.strInstructions
       binding.ingredientsList.text=meal.getIngredientsList()
        binding.measuresList.text=meal.getMeasuresList()
        Log.e("123",meal.getIngredientsList())

        Log.e("124",meal.getMeasuresList())


        meal.strYoutube?.let { youtubeUrl ->
            binding.foodPlayImg.visibility = View.VISIBLE
            binding.foodPlayImg.setOnClickListener {
                openYoutubeVideo(youtubeUrl)
            }
        } ?: run {
            binding.foodPlayImg.visibility = View.GONE
        }
    }

    private fun openYoutubeVideo(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "YouTube app not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        finish()
    }
}