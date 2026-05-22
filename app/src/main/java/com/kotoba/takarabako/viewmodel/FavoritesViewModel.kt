package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.model.Word
import com.kotoba.takarabako.data.repository.QuoteRepository
import com.kotoba.takarabako.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val quoteRepo = QuoteRepository.getInstance(application)
    private val wordRepo = WordRepository.getInstance(application)

    private val _favQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val favQuotes: StateFlow<List<Quote>> = _favQuotes

    private val _favWords = MutableStateFlow<List<Word>>(emptyList())
    val favWords: StateFlow<List<Word>> = _favWords

    private val _activeTab = MutableStateFlow("quote")
    val activeTab: StateFlow<String> = _activeTab

    init {
        quoteRepo.getFavoriteIds().onEach { ids ->
            val allQuotes = quoteRepo.getAll()
            _favQuotes.value = allQuotes.filter { it.id in ids }
        }.launchIn(viewModelScope)

        wordRepo.getFavoriteIds().onEach { ids ->
            val allWords = wordRepo.getAll()
            _favWords.value = allWords.filter { it.id in ids }
        }.launchIn(viewModelScope)
    }

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    fun removeQuote(id: String) {
        viewModelScope.launch { quoteRepo.removeFavorite(id) }
    }

    fun removeWord(id: String) {
        viewModelScope.launch { wordRepo.removeFavorite(id) }
    }
}
