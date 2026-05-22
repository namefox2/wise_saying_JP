package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.repository.QuoteRepository
import com.kotoba.takarabako.util.AppRefreshBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        AppRefreshBus.tick.onEach {
            if (it > 0) {
                loadTodayQuote()
                loadCategoryCounts()
            }
        }.launchIn(viewModelScope)
    }

    fun loadTodayQuote() {
        viewModelScope.launch {
            _todayQuote.value = repository.getTodayQuote()
        }
    }

    private fun loadCategoryCounts() {
        viewModelScope.launch {
            val all = repository.getAll()
            val cats = listOf("노력", "성공", "사랑", "인생", "학습", "마음", "기타")
            _categoryCounts.value = buildMap {
                put("전체", all.size)
                cats.forEach { cat -> put(cat, all.count { it.cat == cat }) }
            }
        }
    }

    fun setHomeCatTab(tab: String) {
        _homeCatTab.value = tab
    }
}
