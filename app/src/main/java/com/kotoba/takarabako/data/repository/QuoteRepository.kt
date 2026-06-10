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
            instance ?: synchronized(this) {
                instance ?: QuoteRepository(context.applicationContext).also { instance = it }
            }
    }

    private val db = AppDatabase.getInstance(context)
    private val dao = db.favoriteDao()
    private val gson = Gson()

    @Volatile private var cachedQuotes: List<Quote>? = null

    private val blockedAuthors = setOf(
        "チャップリン",
        "エーリッヒ・フロム",
        "オードリー・ヘップバーン",
        "パウロ・コエーリョ",
        "ラオウ"
    )

    fun getAll(): List<Quote> {
        cachedQuotes?.let { return it }
        val type = TypeToken.getParameterized(List::class.java, Quote::class.java).type
        val json = context.assets.open("data/quotes.json").bufferedReader().readText()
        cachedQuotes = gson.fromJson<List<Quote>>(json, type)
            .filter { it.author !in blockedAuthors }
        return cachedQuotes!!
    }

    fun getByCategory(cat: String): List<Quote> {
        val all = getAll()
        return if (cat == "전체") all else all.filter { it.cat == cat }
    }

    fun getTodayQuote(): Quote? {
        val all = getAll()
        if (all.isEmpty()) return null
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
