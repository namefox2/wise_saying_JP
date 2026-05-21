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

    private val repository = QuoteRepository(application)

    private val _todayQuote = MutableStateFlow<Quote?>(null)
    val todayQuote: StateFlow<Quote?> = _todayQuote

    private val _homeCatTab = MutableStateFlow("quote")
    val homeCatTab: StateFlow<String> = _homeCatTab

    init {
        loadTodayQuote()
    }

    fun loadTodayQuote() {
        viewModelScope.launch {
            _todayQuote.value = repository.getTodayQuote()
        }
    }

    fun setHomeCatTab(tab: String) {
        _homeCatTab.value = tab
    }
}
