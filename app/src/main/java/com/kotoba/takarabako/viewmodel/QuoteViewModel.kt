package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.local.DataStoreManager
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuoteRepository.getInstance(application)
    private val ds = DataStoreManager(application)

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _likedIds = MutableStateFlow<Set<String>>(emptySet())
    val likedIds: StateFlow<Set<String>> = _likedIds

    private val _bookmarkedId = MutableStateFlow("")
    val bookmarkedId: StateFlow<String> = _bookmarkedId

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled

    private var currentCategory = ""

    init {
        repository.getFavoriteIds().onEach { _likedIds.value = it }.launchIn(viewModelScope)
    }

    fun loadByCategory(category: String) {
        currentCategory = category
        viewModelScope.launch {
            val bmKey = "quote_$category"
            val shuffleKey = "quote_$category"
            val bookmarkId = ds.getBookmark(bmKey).first()
            val shuffleOrderCsv = ds.getShuffleOrder(shuffleKey).first()
            val allQuotes = repository.getByCategory(category)

            val quotes = if (shuffleOrderCsv.isNotEmpty()) {
                val idList = shuffleOrderCsv.split(",")
                val quoteMap = allQuotes.associateBy { it.id }
                idList.mapNotNull { quoteMap[it] }
            } else {
                allQuotes
            }

            _isShuffled.value = shuffleOrderCsv.isNotEmpty()
            _bookmarkedId.value = bookmarkId
            _quotes.value = quotes

            _currentIndex.value = if (bookmarkId.isNotEmpty()) {
                val idx = quotes.indexOfFirst { it.id == bookmarkId }
                if (idx >= 0) idx else 0
            } else 0
        }
    }

    fun shuffle() {
        if (_quotes.value.isEmpty()) return
        val shuffled = _quotes.value.shuffled()
        _quotes.value = shuffled
        _currentIndex.value = 0
        _isShuffled.value = true
        val key = "quote_$currentCategory"
        viewModelScope.launch {
            ds.clearBookmark(key)
            _bookmarkedId.value = ""
            ds.saveShuffleOrder(key, shuffled.joinToString(",") { it.id })
        }
    }

    fun resetOrder() {
        val key = "quote_$currentCategory"
        viewModelScope.launch {
            ds.clearShuffleOrder(key)
            ds.clearBookmark(key)
            _bookmarkedId.value = ""
            _isShuffled.value = false
            _quotes.value = repository.getByCategory(currentCategory)
            _currentIndex.value = 0
        }
    }

    fun next() {
        val size = _quotes.value.size
        if (size == 0) return
        _currentIndex.value = (_currentIndex.value + 1) % size
    }

    fun prev() {
        val size = _quotes.value.size
        if (size == 0) return
        _currentIndex.value = if (_currentIndex.value == 0) size - 1 else _currentIndex.value - 1
    }

    fun toggleBookmark() {
        val quote = _quotes.value.getOrNull(_currentIndex.value) ?: return
        val key = "quote_$currentCategory"
        viewModelScope.launch {
            if (_bookmarkedId.value == quote.id) {
                ds.clearBookmark(key)
                _bookmarkedId.value = ""
            } else {
                ds.setBookmark(key, quote.id)
                _bookmarkedId.value = quote.id
            }
        }
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (_likedIds.value.contains(id)) repository.removeFavorite(id)
            else repository.addFavorite(id)
        }
    }
}
