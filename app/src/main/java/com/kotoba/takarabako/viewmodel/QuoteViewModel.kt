package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuoteRepository.getInstance(application)

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _likedIds = MutableStateFlow<Set<String>>(emptySet())
    val likedIds: StateFlow<Set<String>> = _likedIds

    init {
        repository.getFavoriteIds().onEach { _likedIds.value = it }.launchIn(viewModelScope)
    }

    fun loadByCategory(category: String) {
        _quotes.value = repository.getByCategory(category)
        _currentIndex.value = 0
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

    fun shuffle() {
        if (_quotes.value.isEmpty()) return
        _quotes.value = _quotes.value.shuffled()
        _currentIndex.value = 0
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (_likedIds.value.contains(id)) repository.removeFavorite(id)
            else repository.addFavorite(id)
        }
    }
}
