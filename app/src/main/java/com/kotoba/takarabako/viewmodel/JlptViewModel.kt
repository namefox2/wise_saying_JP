package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.local.DataStoreManager
import com.kotoba.takarabako.data.model.Word
import com.kotoba.takarabako.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class JlptViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WordRepository.getInstance(application)
    private val ds = DataStoreManager.getInstance(application)

    private val _currentLevel = MutableStateFlow("all")
    val currentLevel: StateFlow<String> = _currentLevel

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _likedWordIds = MutableStateFlow<Set<String>>(emptySet())
    val likedWordIds: StateFlow<Set<String>> = _likedWordIds

    private val _bookmarkedId = MutableStateFlow("")
    val bookmarkedId: StateFlow<String> = _bookmarkedId

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled

    init {
        repository.getFavoriteIds().onEach { _likedWordIds.value = it }.launchIn(viewModelScope)
        setLevel("all")
    }

    fun setLevel(level: String) {
        _currentLevel.value = level
        _currentIndex.value = 0
        viewModelScope.launch {
            val bmKey = "jlpt_$level"
            val shuffleKey = "jlpt_$level"
            val bookmarkId = ds.getBookmark(bmKey).first()
            val shuffleOrderCsv = ds.getShuffleOrder(shuffleKey).first()
            val allWords = repository.getByLevel(level)

            val words = if (shuffleOrderCsv.isNotEmpty()) {
                val idList = shuffleOrderCsv.split(",")
                val wordMap = allWords.associateBy { it.id }
                idList.mapNotNull { wordMap[it] }
            } else {
                allWords
            }

            _isShuffled.value = shuffleOrderCsv.isNotEmpty()
            _bookmarkedId.value = bookmarkId
            _words.value = words

            if (bookmarkId.isNotEmpty()) {
                val idx = words.indexOfFirst { it.id == bookmarkId }
                if (idx >= 0) _currentIndex.value = idx
            }
        }
    }

    fun shuffle() {
        if (_words.value.isEmpty()) return
        val shuffled = _words.value.shuffled()
        _words.value = shuffled
        _currentIndex.value = 0
        _isShuffled.value = true
        val key = "jlpt_${_currentLevel.value}"
        viewModelScope.launch {
            ds.clearBookmark(key)
            _bookmarkedId.value = ""
            ds.saveShuffleOrder(key, shuffled.joinToString(",") { it.id })
        }
    }

    fun resetOrder() {
        val key = "jlpt_${_currentLevel.value}"
        viewModelScope.launch {
            ds.clearShuffleOrder(key)
            ds.clearBookmark(key)
            _bookmarkedId.value = ""
            _isShuffled.value = false
            _words.value = repository.getByLevel(_currentLevel.value)
            _currentIndex.value = 0
        }
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

    fun toggleBookmark() {
        val word = _words.value.getOrNull(_currentIndex.value) ?: return
        val key = "jlpt_${_currentLevel.value}"
        viewModelScope.launch {
            if (_bookmarkedId.value == word.id) {
                ds.clearBookmark(key)
                _bookmarkedId.value = ""
            } else {
                ds.setBookmark(key, word.id)
                _bookmarkedId.value = word.id
            }
        }
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (_likedWordIds.value.contains(id)) repository.removeFavorite(id)
            else repository.addFavorite(id)
        }
    }
}
