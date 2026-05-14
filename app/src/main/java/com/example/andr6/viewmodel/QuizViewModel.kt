package com.example.andr6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.andr6.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuizData()
    }

    fun loadQuizData() {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                repository.refreshQuestions()
                repository.allQuestions.collect { questions ->
                    if (questions.isEmpty()) {
                        _uiState.value = QuizUiState.Error("Дані відсутні")
                    } else {
                        _uiState.value = QuizUiState.Success(questions)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = QuizUiState.Error("Помилка: ${e.message}")
            }
        }
    }
}