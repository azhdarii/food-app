package com.azhdar.myapplication5.presentation.foodlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.azhdar.myapplication5.data.model.remote.Category
import com.azhdar.myapplication5.data.model.remote.Food
import com.azhdar.myapplication5.data.repository.Repository
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository()

    private val _randomMeal = MutableLiveData<Food?>()
    val randomMeal: LiveData<Food?> = _randomMeal

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _mealsByCategory = MutableLiveData<List<Food?>>()
    val mealsByCategory: LiveData<List<Food?>> = _mealsByCategory

    private val _mealsBySearch = MutableLiveData<List<Food?>>()
    val mealsBySearch:LiveData<List<Food?>> = _mealsBySearch

    private val _selectedMeal = MutableLiveData<Food?>()
    val selectedMeal: LiveData<Food?> = _selectedMeal


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _mealsToShow = MutableLiveData<List<Food?>>()
    val mealsToShow:LiveData<List<Food?>> = _mealsToShow

     var currentCategory: String? = null

    fun getProperFoodList(currentCategory: String){
        if(currentCategory==null){
            fetchMealBySearch("a")
            _mealsToShow.value=_mealsBySearch.value

        }
        else{
            fetchMealsByCategory(currentCategory)
            _mealsToShow.value=_mealsByCategory.value
        }

    }

    // Detail screen functions
    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedMeal.value = repository.getMealDetails(mealId)
            } catch (e: Exception) {
                _error.value = "Failed to load meal details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedMeal() {
        _selectedMeal.value = null
    }

    fun clearError() {
        _error.value = null
    }




    fun fetchRandomMeal() {
        viewModelScope.launch {
            _randomMeal.value = repository.getRandomMeal()
        }
    }


    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categories.value = repository.getCategories() ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load categories"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMealsByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _mealsByCategory.value = repository.getMealsByCategory(category) ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load meals"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMealBySearch(searchText: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _mealsBySearch.value = repository.getMealsBySearch(searchText) ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Search failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

}