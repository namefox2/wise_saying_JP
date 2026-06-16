package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.model.Word
import com.kotoba.takarabako.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class JlptViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WordRepository.getInstance(application)

    private val _currentLevel = MutableStateFlow("all")
    val currentLevel: StateFlow<String> = _currentLevel

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _likedWordIds = MutableStateFlow<Set<String>>(emptySet())
    val likedWordIds: StateFlow<Set<String>> = _likedWordIds

    init {
        repository.getFavoriteIds().onEach { _likedWordIds.value = it }.launchIn(viewModelScope)
        setLevel("all")
    }

    fun setLevel(level: String) {
        _currentLevel.value = level
        _currentIndex.value = 0
        viewModelScope.launch {
            _words.value = repository.getByLevel(level).shuffled()
        }
    }

    fun shuffle() {
        if (_words.value.isEmpty()) return
        _words.value = _words.value.shuffled()
        _currentIndex.value = 0
    }

    fun next() {
        val size = _words.value.size
        if (size == 0) return
        _currentIndex.value = (_currentIndex.value + 1) % size
    }

    fun prev() {
        val size = _words.value.size
        if (size == 0) return
        _currentIndex.value = if (_currentIndex.value == 0) size - 1 else _currentIndex.value - 1
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (_likedWordIds.value.contains(id)) repository.removeFavorite(id)
            else repository.addFavorite(id)
        }
    }
}
