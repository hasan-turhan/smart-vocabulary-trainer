package com.example.app.navigation

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Flashcards : Screen("flashcards")
    object AddWord : Screen("add_word")
    object MyWords : Screen("my_words")
    object Favorites : Screen("favorites")

}
