package com.example.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.data.UserWordRepository
import com.example.app.data.Word
import com.example.app.data.WordRepository



class FlashcardViewModel : ViewModel() {

    var isShuffleOn by mutableStateOf(false)
        private set

    private val favoritesVersion = mutableIntStateOf(0)
    private val mode = mutableStateOf(FlashcardMode.ALL)
    private val index = mutableIntStateOf(0)
    private val words = mutableStateOf<List<Word>>(emptyList())

    // Public properties for the Screen
    val currentIndex: Int get() = index.intValue
    val totalWords: Int get() = words.value.size
    val currentWord: Word? get() = words.value.getOrNull(index.intValue)

    init {
        reloadWords()
    }

    fun setMode(newMode: FlashcardMode) {
        mode.value = newMode
        index.intValue = 0
        reloadWords()
    }

    fun toggleShuffle() {
        isShuffleOn = !isShuffleOn
        index.intValue = 0 // Reset to start
        reloadWords()
    }

    fun reloadWords() {
        // Get words
        val all = try {
            WordRepository.words + UserWordRepository.getAllWords()
        } catch (e: Exception) {
            emptyList<Word>()
        }


        val filtered = when (mode.value) {
            FlashcardMode.ALL -> all
            FlashcardMode.FAVORITES -> all.filter { UserWordRepository.isFavorite(it) }
        }

        // Apply Shuffle if active
        words.value = if (isShuffleOn) {
            filtered.shuffled()
        } else {
            filtered
        }

        // Safety check
        if (index.intValue >= words.value.size) {
            index.intValue = 0
        }
    }

    fun toggleFavorite(word: Word) {
        UserWordRepository.toggleFavorite(word)
        favoritesVersion.intValue++ // Force refresh
        reloadWords()
    }

    fun isFavorite(word: Word): Boolean {
        favoritesVersion.intValue // Read to subscribe
        return UserWordRepository.isFavorite(word)
    }

    fun next() {
        if (words.value.isEmpty()) return
        if (index.intValue < words.value.lastIndex) index.intValue++ else index.intValue = 0
    }

    fun previous() {
        if (words.value.isEmpty()) return
        if (index.intValue > 0) index.intValue-- else index.intValue = words.value.lastIndex
    }
}