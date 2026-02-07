package com.example.app.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserWordRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var dataStore: DataStoreManager

    private val userWords = mutableListOf<Word>()
    private val favorites = mutableSetOf<String>()

    fun init(store: DataStoreManager) {
        dataStore = store
        scope.launch {
            userWords.addAll(dataStore.loadWords())
            favorites.addAll(dataStore.loadFavorites())
        }
    }

    fun getAllWords(): List<Word> = userWords

    fun addWord(word: Word) {
        userWords.add(word)
        persistWords()
    }

    fun removeWord(word: Word) {
        userWords.remove(word)
        favorites.remove(word.id)
        persistWords()
        persistFavorites()
    }

    // ✅ Fixes "Unresolved reference: toggleFavorite"
    fun toggleFavorite(word: Word) {
        if (favorites.contains(word.id)) {
            favorites.remove(word.id)
        } else {
            favorites.add(word.id)
        }
        persistFavorites()
    }

    fun isFavorite(word: Word): Boolean =
        favorites.contains(word.id)

    private fun persistWords() {
        if (::dataStore.isInitialized) {
            scope.launch { dataStore.saveWords(userWords) }
        }
    }

    private fun persistFavorites() {
        if (::dataStore.isInitialized) {
            scope.launch { dataStore.saveFavorites(favorites) }
        }
    }
}