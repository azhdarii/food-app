package com.azhdar.myapplication5.data.repository

import com.azhdar.myapplication5.data.remote.ApiServices
import com.azhdar.myapplication5.data.model.remote.Category
import com.azhdar.myapplication5.data.model.remote.Food

class Repository {
    private val mealApiService = ApiServices.create()

    suspend fun getRandomMeal(): Food? {
        return mealApiService.getRandomMeal().meals.firstOrNull()
    }

    suspend fun getCategories(): List<Category> {
        return mealApiService.getCategories().categories
    }

    suspend fun getMealsByCategory(category: String): List<Food> {
        return mealApiService.getMealsByCategory(category).meals
    }

    suspend fun getMealDetails(mealId: String): Food? {
        return mealApiService.getMealDetails(mealId).meals.firstOrNull()
    }

    suspend fun getMealsBySearch(searchText:String):List<Food>{
        return mealApiService.getMealsbySearch(searchText).meals
    }

}