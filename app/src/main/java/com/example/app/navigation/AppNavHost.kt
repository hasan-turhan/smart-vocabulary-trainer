package com.example.app.navigation

import com.example.app.viewmodel.FlashcardMode
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.screens.flashcards.FlashcardScreen
import com.example.app.screens.menu.MenuScreen
import com.example.app.screens.mywords.AddWordScreen
import com.example.app.screens.mywords.MyWordsListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Menu.route
    ) {

        composable(Screen.Menu.route) {
            MenuScreen(
                onLearnClick = { navController.navigate(Screen.Flashcards.route) },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                onAddWordClick = { navController.navigate(Screen.AddWord.route) },
                onMyWordsClick = { navController.navigate(Screen.MyWords.route) }
            )
        }
        composable(Screen.Flashcards.route) {
            FlashcardScreen(mode = FlashcardMode.ALL)
        }
        composable(Screen.Favorites.route) {
            FlashcardScreen(mode = FlashcardMode.FAVORITES)
        }



        composable(Screen.Flashcards.route) {
            FlashcardScreen()
        }

        composable(Screen.AddWord.route) {
            AddWordScreen(
                onSave = { navController.popBackStack() }
            )
        }

        composable(Screen.MyWords.route) {
            MyWordsListScreen()
        }
    }
}
