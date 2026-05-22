package com.kotoba.takarabako.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotoba.takarabako.data.local.AppDatabase
import com.kotoba.takarabako.data.local.FavoriteEntity
import com.kotoba.takarabako.data.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate

class QuoteRepository private constructor(private val context: Context) {

    companion object {
        private const val REMOTE_URL =
            "https://raw.githubusercontent.com/namefox2/wise_saying_jp/claude/store-release-prep-RhLsd/app/src/main/assets/data/quotes.json"
        private const val LOCAL_FILE = "quotes.json"

        @Volatile private var instance: QuoteRepository? = null
        fun getInstance(context: Context): QuoteRepository =
            instance ?: synchronized(this) {
                instance ?: QuoteRepository(context.applicationContext).also { instance = it }
            }
    }

    private val db = AppDatabase.getInstance(context)
    private val dao = db.favoriteDao()
    private val gson = Gson()

    @Volatile private var cachedQuotes: List<Quote>? = null

    fun clearCache() {
        cachedQuotes = null
        localFile().delete()
    }

    private val blockedAuthors = setOf(
        "チャップリン",
        "エーリッヒ・フロム",
        "オードリー・ヘップバーン",
        "パウロ・コエーリョ",
        "ラオウ"
    )

    private fun localFile() = File(context.filesDir, LOCAL_FILE)

    private fun readJson(): String {
        val local = localFile()
        return if (local.exists()) local.readText()
        else context.assets.open("data/quotes.json").bufferedReader().readText()
    }

    fun getAll(): List<Quote> {
        cachedQuotes?.let { return it }
        val type = object : TypeToken<List<Quote>>() {}.type
        cachedQuotes = gson.fromJson<List<Quote>>(readJson(), type)
            .filter { it.author !in blockedAuthors }
        return cachedQuotes!!
    }

    suspend fun fetchAndSave(): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = java.net.URL(REMOTE_URL).openConnection().apply {
                connectTimeout = 8000
                readTimeout = 8000
            }.getInputStream().bufferedReader().readText()
            localFile().writeText(json)
            clearCache()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getByCategory(cat: String): List<Quote> {
        val all = getAll()
        return if (cat == "전체") all else all.filter { it.cat == cat }
    }

    fun getTodayQuote(): Quote {
        val all = getAll()
        val dayOfYear = LocalDate.now().dayOfYear
        return all[dayOfYear % all.size]
    }

    fun getFavoriteIds(): Flow<Set<String>> =
        dao.getAllIds("quote").map { it.toSet() }

    suspend fun addFavorite(id: String) {
        dao.insert(FavoriteEntity(id, "quote"))
    }

    suspend fun removeFavorite(id: String) {
        dao.delete(FavoriteEntity(id, "quote"))
    }
}
