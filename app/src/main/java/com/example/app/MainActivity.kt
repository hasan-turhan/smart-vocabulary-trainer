package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.app.data.DataStoreManager
import com.example.app.data.UserWordRepository
import com.example.app.navigation.AppNavHost
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreManager(this)
        UserWordRepository.init(dataStore) // 🔑 REQUIRED

        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
}

