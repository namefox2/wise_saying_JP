package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuoteRepository.getInstance(application)

    private val _todayQuote = MutableStateFlow<Quote?>(null)
    val todayQuote: StateFlow<Quote?> = _todayQuote

    private val _homeCatTab = MutableStateFlow("quote")
    val homeCatTab: StateFlow<String> = _homeCatTab

    private val _categoryCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val categoryCounts: StateFlow<Map<String, Int>> = _categoryCounts

    init {
        loadTodayQuote()
        loadCategoryCounts()
    }

    fun loadTodayQuote() {
        viewModelScope.launch {
            _todayQuote.value = repository.getTodayQuote()
        }
    }

    private fun loadCategoryCounts() {
        viewModelScope.launch {
            val all = repository.getAll()
            val countsByCat = all.groupingBy { it.cat }.eachCount()
            _categoryCounts.value = buildMap {
                put("전체", all.size)
                countsByCat.forEach { (cat, count) -> put(cat, count) }
            }
        }
    }

    fun setHomeCatTab(tab: String) {
        _homeCatTab.value = tab
    }
}
