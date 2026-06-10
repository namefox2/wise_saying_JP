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
        val allQuotes = quoteRepo.getAll()
        val allWords = wordRepo.getAll()
        quoteRepo.getFavoriteIds().onEach { ids ->
            _favQuotes.value = allQuotes.filter { it.id in ids }
        }.launchIn(viewModelScope)
        wordRepo.getFavoriteIds().onEach { ids ->
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

    fun buildExportText(): String {
        val sb = StringBuilder()
        sb.append("=== 즐겨찾기 명언 ===\n\n")
        _favQuotes.value.forEach { q ->
            sb.append("${q.kanji}\n")
            sb.append("${q.korean}\n")
            sb.append("— ${q.author}\n\n")
        }
        sb.append("=== 저장한 단어 ===\n\n")
        _favWords.value.forEach { w ->
            sb.append("${w.kanji} (${w.reading}): ${w.meaning}\n")
        }
        return sb.toString()
    }
}
