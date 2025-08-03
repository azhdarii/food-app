package com.azhdar.myapplication5.presentation.fooddetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.azhdar.myapplication5.R
import com.azhdar.myapplication5.data.local.AppDatabase
import com.azhdar.myapplication5.data.model.remote.Food
import com.azhdar.myapplication5.data.repository.FavoriteRepository
import com.azhdar.myapplication5.databinding.ActivityFoodDetailBinding
import com.azhdar.myapplication5.presentation.foodlist.MealViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FoodDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var mealViewModel: MealViewModel
    private lateinit var foodDetailViewModel: FoodDetailViewModel
    private lateinit var currentMeal: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModels
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)
        val factory = FoodDetailViewModelFactory(
            FavoriteRepository(
                AppDatabase.getDatabase(this).favoriteDao()
            )
        )

        foodDetailViewModel = ViewModelProvider(this, factory).get(FoodDetailViewModel::class.java)
        val json = intent.getStringExtra("MEAL_JSON")
        val currentMeal = Gson().fromJson(json, Food::class.java)

        // Setup observers
        setupObservers()
        setupFavoriteButton(currentMeal)

        binding.backButton.setOnClickListener { v ->
            // Handle back button click
            onBackPressedDispatcher.onBackPressed(); // Recommended for modern Android
        };

        // Fetch meal details
        mealViewModel.fetchMealDetails(currentMeal.idMeal)
    }

    private fun setupFavoriteButton(meal: Food) {
        // Observe favorite state changes
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodDetailViewModel.isFavorite.collectLatest { isFavorite ->
                    updateFavoriteButtonAppearance(isFavorite)
                }
            }
        }

        // Initial check
        lifecycleScope.launch {
            foodDetailViewModel.checkFavoriteStatus(meal.idMeal)
        }

        // Set click listener
        binding.favoriteButton.setOnClickListener {
            currentMeal?.let { meal ->
                foodDetailViewModel.toggleFavorite(meal)
            }
        }
    }

    private fun updateFavoriteButtonAppearance(isFavorite: Boolean) {
        binding.favoriteButton.apply {
            setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )
            imageTintList = ContextCompat.getColorStateList(
                this@FoodDetailActivity,
                if (isFavorite) R.color.favorite_red
                else R.color.black
            )
        }
    }

    private fun setupObservers() {
        mealViewModel.selectedMeal.observe(this) { meal ->
            meal?.let {
                currentMeal = it
                displayMealDetails(it)
            }
        }

        mealViewModel.isLoading.observe(this) { isLoading ->
            // Show/hide loading indicator
            //binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mealViewModel.error.observe(this) { error ->
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
        binding.ingredientsList.text = meal.getIngredientsList()
        binding.measuresList.text = meal.getMeasuresList()

        // Handle YouTube link
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