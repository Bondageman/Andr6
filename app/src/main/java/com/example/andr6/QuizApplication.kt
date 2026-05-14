package com.example.andr6

import android.app.Application
import com.example.andr6.local.AppDatabase
import com.example.andr6.network.RetrofitInstance
import com.example.andr6.repository.QuizRepository

class QuizApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { QuizRepository(RetrofitInstance.api, database.questionDao()) }
}