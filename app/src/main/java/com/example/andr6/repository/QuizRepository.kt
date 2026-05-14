package com.example.andr6.repository

import com.example.andr6.local.QuestionDao
import com.example.andr6.local.QuestionEntity
import com.example.andr6.network.QuizApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuizRepository(
    private val api: QuizApi,
    private val dao: QuestionDao
) {
    val allQuestions: Flow<List<QuestionEntity>> = dao.getAllQuestions()

    suspend fun refreshQuestions() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getQuestions(amount = 10)

                val entities = response.results.map { networkModel ->
                    QuestionEntity(
                        category = networkModel.category,
                        difficulty = networkModel.difficulty,
                        question = networkModel.question,
                        correctAnswer = networkModel.correctAnswer,
                        incorrectAnswers = networkModel.incorrectAnswers
                    )
                }

                dao.clearQuestions()
                dao.insertQuestions(entities)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}