package com.example.andr6.screens

import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andr6.local.QuestionEntity
import com.example.andr6.viewmodel.QuizUiState
import com.example.andr6.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Master") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is QuizUiState.Loading -> CircularProgressIndicator()
                is QuizUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadQuizData() }) { Text("Retry") }
                    }
                }
                is QuizUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.questions) { question ->
                            QuestionItem(question)
                        }
                        item {
                            Button(
                                onClick = { viewModel.loadMore() },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                            ) {
                                Text("Load More Questions")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionItem(question: QuestionEntity) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    val allAnswers = remember(question) {
        (question.incorrectAnswers + question.correctAnswer).shuffled()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = Html.fromHtml(question.question, Html.FROM_HTML_MODE_COMPACT).toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    allAnswers.forEach { answer ->
                        val isCorrect = answer == question.correctAnswer
                        val isSelected = answer == selectedAnswer

                        val containerColor = when {
                            selectedAnswer == null -> MaterialTheme.colorScheme.surfaceVariant
                            isCorrect -> Color(0xFF4CAF50)
                            isSelected -> Color(0xFFF44336)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        Button(
                            onClick = { if (selectedAnswer == null) selectedAnswer = answer },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerColor,
                                contentColor = if (selectedAnswer != null && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(text = Html.fromHtml(answer, Html.FROM_HTML_MODE_COMPACT).toString())
                        }
                    }
                }
            }

            if (!isExpanded) {
                Text(
                    text = "Tap to show options",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}