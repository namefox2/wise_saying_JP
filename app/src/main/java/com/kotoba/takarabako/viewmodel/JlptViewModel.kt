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

    private val repository = WordRepository(application)

    private val _currentLevel = MutableStateFlow("all")
    val currentLevel: StateFlow<String> = _currentLevel

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words

    private val _likedWordIds = MutableStateFlow<Set<String>>(emptySet())
    val likedWordIds: StateFlow<Set<String>> = _likedWordIds

    init {
        repository.getFavoriteIds().onEach { _likedWordIds.value = it }.launchIn(viewModelScope)
        setLevel("all")
    }

    fun setLevel(level: String) {
        _currentLevel.value = level
        viewModelScope.launch {
            _words.value = repository.getByLevel(level)
        }
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (_likedWordIds.value.contains(id)) {
                repository.removeFavorite(id)
            } else {
                repository.addFavorite(id)
            }
        }
    }
}
