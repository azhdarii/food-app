package com.azhdar.myapplication5.data.remote

import com.azhdar.myapplication5.data.model.remote.CategoryResponse
import com.azhdar.myapplication5.data.model.remote.FoodResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

        @GET("api/json/v1/1/random.php")
        suspend fun getRandomMeal(): FoodResponse

        @GET("api/json/v1/1/categories.php")
        suspend fun getCategories(): CategoryResponse

        @GET("api/json/v1/1/filter.php")
        suspend fun getMealsByCategory(@Query("c") category: String): FoodResponse

        @GET("api/json/v1/1/lookup.php")
        suspend fun getMealDetails(@Query("i") mealId: String): FoodResponse

        @GET("api/json/v1/1/search.php")
        suspend fun getMealsbySearch(@Query("f") searchText: String): FoodResponse

        companion object {
            const val BASE_URL = "https://www.themealdb.com/"

            fun create(): ApiServices {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit.create(ApiServices::class.java)
            }
        }

}
