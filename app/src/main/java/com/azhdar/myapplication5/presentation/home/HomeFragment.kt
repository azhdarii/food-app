package com.azhdar.myapplication5.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhdar.myapplication5.databinding.FragmentHomeBinding
import com.azhdar.myapplication5.presentation.categorylist.CategoriesAdapter
import com.azhdar.myapplication5.presentation.fooddetail.FoodDetailActivity
import com.azhdar.myapplication5.presentation.foodlist.FoodAdapter
import com.azhdar.myapplication5.presentation.foodlist.MealViewModel
import com.google.gson.Gson

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            viewModel = ViewModelProvider(requireActivity()).get(MealViewModel::class.java)

            // Initialize Adapters
            categoriesAdapter = CategoriesAdapter(emptyList()) { category ->
                viewModel.mealsByCategory.removeObservers(viewLifecycleOwner)
                viewModel.mealsBySearch.removeObservers(viewLifecycleOwner)

                if (viewModel.currentCategory == category.strCategory) {
                    viewModel.currentCategory = null
                    viewModel.fetchMealBySearch("a")
                    viewModel.mealsBySearch.observe(viewLifecycleOwner) { meals ->
                        foodAdapter.updateList(meals)
                    }
                } else {
                    viewModel.currentCategory = category.strCategory
                    viewModel.fetchMealsByCategory(category.strCategory)
                    viewModel.mealsByCategory.observe(viewLifecycleOwner) { meals ->
                        foodAdapter.updateList(meals)
                    }
                }
            }

            foodAdapter = FoodAdapter(emptyList()) { meal ->
                meal?.let { nonNullMeal ->
                    val intent = Intent(requireContext(), FoodDetailActivity::class.java).apply {
                        putExtra("MEAL_JSON", Gson().toJson(nonNullMeal))  // Fixed variable name
                    }
                    startActivity(intent)
                }
            }

            // Setup RecyclerViews
            binding.categoriesRecycler.apply {
                adapter = categoriesAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            binding.foodsRecycler.apply {
                adapter = foodAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            // Observe LiveData
            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                categories?.let { categoriesAdapter.updateList(it) }
            }

            viewModel.mealsBySearch.observe(viewLifecycleOwner) { meals ->
                meals?.let { foodAdapter.updateList(it) }
            }

            // Load initial data
            viewModel.fetchCategories()
            viewModel.fetchMealBySearch("a")

        } catch (e: Exception) {
            Log.e("HomeFragment", e.toString())
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}