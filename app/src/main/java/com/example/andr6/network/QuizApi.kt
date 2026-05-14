package com.example.andr6.network
import com.example.andr6.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10 // Передаємо параметр amount=10
    ): QuizResponse
}