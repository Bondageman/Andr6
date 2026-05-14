package com.example.andr6.repository

import com.example.andr6.local.QuestionDao
import com.example.andr6.local.QuestionEntity
import com.example.andr6.network.QuizApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(private val api: QuizApi, private val dao: QuestionDao) {
    val allQuestions = dao.getAllQuestions()

    suspend fun refreshQuestions() {
        withContext(Dispatchers.IO) {
            val response = api.getQuestions(10)
            val entities = response.results.map {
                QuestionEntity(
                    category = it.category,
                    difficulty = it.difficulty,
                    question = it.question,
                    correctAnswer = it.correctAnswer,
                    incorrectAnswers = it.incorrectAnswers
                )
            }
            dao.clearQuestions()
            dao.insertQuestions(entities)
        }
    }

    suspend fun loadMoreQuestions() {
        withContext(Dispatchers.IO) {
            val response = api.getQuestions(10)
            val entities = response.results.map {
                QuestionEntity(
                    category = it.category,
                    difficulty = it.difficulty,
                    question = it.question,
                    correctAnswer = it.correctAnswer,
                    incorrectAnswers = it.incorrectAnswers
                )
            }
            dao.insertQuestions(entities)
        }
    }
}