package com.kotoba.takarabako.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotoba.takarabako.data.local.AppDatabase
import com.kotoba.takarabako.data.local.FavoriteEntity
import com.kotoba.takarabako.data.model.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRepository private constructor(private val context: Context) {

    companion object {
        @Volatile private var instance: WordRepository? = null
        fun getInstance(context: Context): WordRepository =
            instance ?: synchronized(this) {
                instance ?: WordRepository(context.applicationContext).also { instance = it }
            }
    }

    private val db = AppDatabase.getInstance(context)
    private val dao = db.favoriteDao()
    private val gson = Gson()

    private val levelFiles = mapOf(
        "N1" to "words_n1.json",
        "N2" to "words_n2.json",
        "N3" to "words_n3.json",
        "N4" to "words_n4.json",
        "N5" to "words_n5.json"
    )

    private val cache = mutableMapOf<String, List<Word>>()

    fun getByLevel(level: String): List<Word> {
        if (level == "all") return getAll()
        cache[level]?.let { return it }
        val filename = levelFiles[level] ?: return emptyList()
        val type = object : TypeToken<List<Word>>() {}.type
        val json = context.assets.open("data/$filename").bufferedReader().readText()
        val words: List<Word> = gson.fromJson(json, type)
        cache[level] = words
        return words
    }

    fun getAll(): List<Word> = levelFiles.keys.flatMap { getByLevel(it) }

    fun getFavoriteIds(): Flow<Set<String>> =
        dao.getAllIds("word").map { it.toSet() }

    suspend fun addFavorite(id: String) {
        dao.insert(FavoriteEntity(id, "word"))
    }

    suspend fun removeFavorite(id: String) {
        dao.delete(FavoriteEntity(id, "word"))
    }
}
