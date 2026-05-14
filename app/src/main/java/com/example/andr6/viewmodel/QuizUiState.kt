package com.example.andr6.viewmodel

import com.example.andr6.local.QuestionEntity

sealed interface QuizUiState {
    object Loading : QuizUiState
    data class Success(val questions: List<QuestionEntity>) : QuizUiState
    data class Error(val message: String) : QuizUiState
}
