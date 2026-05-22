package com.kotoba.takarabako.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotoba.takarabako.data.local.AppDatabase
import com.kotoba.takarabako.data.local.FavoriteEntity
import com.kotoba.takarabako.data.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class WordRepository private constructor(private val context: Context) {

    companion object {
        private const val BASE_URL =
            "https://raw.githubusercontent.com/namefox2/wise_saying_jp/main/app/src/main/assets/data/"

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

    fun clearCache() { cache.clear() }

    private fun localFile(filename: String) = File(context.filesDir, filename)

    private fun readJson(level: String): String {
        val filename = levelFiles[level] ?: return "[]"
        val local = localFile(filename)
        return if (local.exists()) local.readText()
        else context.assets.open("data/$filename").bufferedReader().readText()
    }

    fun getByLevel(level: String): List<Word> {
        if (level == "all") return getAll()
        cache[level]?.let { return it }
        val type = object : TypeToken<List<Word>>() {}.type
        val words: List<Word> = gson.fromJson(readJson(level), type)
        cache[level] = words
        return words
    }

    suspend fun fetchAndSave(): Boolean = withContext(Dispatchers.IO) {
        var anySuccess = false
        levelFiles.forEach { (_, filename) ->
            try {
                val json = java.net.URL("$BASE_URL$filename").openConnection().apply {
                    connectTimeout = 8000
                    readTimeout = 8000
                }.getInputStream().bufferedReader().readText()
                localFile(filename).writeText(json)
                anySuccess = true
            } catch (_: Exception) {}
        }
        if (anySuccess) clearCache()
        anySuccess
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
