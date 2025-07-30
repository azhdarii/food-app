package com.azhdar.myapplication5.data.model.remote
data class Food(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strYoutube: String?,
    val strTags: String?,

    // Ingredients and measures (up to 20)
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient20: String?,

    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure20: String?
) {





    fun getIngredientsList(): String {
        return listOf(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
        ).filterNotNull().filter { it.isNotBlank() }.joinToString("\n")
    }

    // Get list of non-null measures
    fun getMeasuresList(): String{
        return listOf(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
        ).filterNotNull().filter { it.isNotBlank() }.joinToString("\n")
    }




    fun getIngredientsWithMeasures(): String {
        val ingredientsList = mutableListOf<String>()

        for (i in 1..20) {
            try {
                val ingredientField = this::class.java.getDeclaredField("strIngredient$i")
                val measureField = this::class.java.getDeclaredField("strMeasure$i")

                val ingredient = ingredientField.get(this) as? String
                val measure = measureField.get(this) as? String

                if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                    ingredientsList.add("$ingredient   :    $measure\n")
                }
            } catch (e: Exception) {
                // Field doesn't exist or other reflection error
            }
        }

        return ingredientsList.joinToString(", ")
    }

    // Alternative version that returns List<Pair> if you need structured data
    fun getIngredientsWithMeasuresList(): List<Pair<String, String>> {
        return (1..20).mapNotNull { i ->
            try {
                val ingredient = this::class.java.getDeclaredField("strIngredient$i").get(this) as? String
                val measure = this::class.java.getDeclaredField("strMeasure$i").get(this) as? String

                if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                    Pair(ingredient, measure)
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}

data class FoodResponse(
    val meals: List<Food>
)