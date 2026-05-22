package com.kotoba.takarabako.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotoba.takarabako.data.local.AppDatabase
import com.kotoba.takarabako.data.local.FavoriteEntity
import com.kotoba.takarabako.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class QuoteRepository private constructor(private val context: Context) {

    companion object {
        @Volatile private var instance: QuoteRepository? = null
        fun getInstance(context: Context): QuoteRepository =
            instance ?: synchronized(this) { instance ?: QuoteRepository(context.applicationContext).also { instance = it } }
    }

    private val db = AppDatabase.getInstance(context)
    private val dao = db.favoriteDao()
    private val gson = Gson()

    @Volatile private var cachedQuotes: List<Quote>? = null

    fun clearCache() { cachedQuotes = null }

    // Authors still under copyright — blocked regardless of data source
    private val blockedAuthors = setOf(
        "チャップリン",            // died 1977, expires 2047
        "エーリッヒ・フロム",      // died 1980, expires 2050
        "オードリー・ヘップバーン", // died 1993, expires 2063
        "パウロ・コエーリョ",      // living author
        "ラオウ"                   // copyrighted manga character (北斗の拳)
    )

    fun getAll(): List<Quote> {
        if (cachedQuotes != null) return cachedQuotes!!
        val json = context.assets.open("data/quotes.json").bufferedReader().readText()
        val type = object : TypeToken<List<Quote>>() {}.type
        cachedQuotes = gson.fromJson<List<Quote>>(json, type)
            .filter { it.author !in blockedAuthors }
        return cachedQuotes!!
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
