package com.example.andr6
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.andr6.QuizApplication
import com.example.andr6.screens.QuizScreen
import com.example.andr6.ui.theme.Andr6Theme
import com.example.andr6.viewmodel.QuizViewModel
import com.example.andr6.viewmodel.QuizViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as QuizApplication
        val repository = app.repository

        val viewModel: QuizViewModel by viewModels {
            QuizViewModelFactory(repository)
        }

        setContent {
            Andr6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizScreen(viewModel = viewModel)
                }
            }
        }
    }
}