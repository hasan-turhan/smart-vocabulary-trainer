package com.example.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("app_store")

class DataStoreManager(private val context: Context) {

    private val gson = Gson()

    private val WORDS_KEY = stringPreferencesKey("user_words")
    private val FAVORITES_KEY = stringPreferencesKey("favorites")

    suspend fun saveWords(words: List<Word>) {
        context.dataStore.edit {
            it[WORDS_KEY] = gson.toJson(words)
        }
    }

    suspend fun loadWords(): List<Word> {
        val json = context.dataStore.data.first()[WORDS_KEY] ?: return emptyList()
        val type = object : TypeToken<List<Word>>() {}.type
        return gson.fromJson(json, type)
    }

    suspend fun saveFavorites(ids: Set<String>) {
        context.dataStore.edit {
            it[FAVORITES_KEY] = gson.toJson(ids)
        }
    }

    suspend fun loadFavorites(): Set<String> {
        val json = context.dataStore.data.first()[FAVORITES_KEY] ?: return emptySet()
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type)
    }
}
