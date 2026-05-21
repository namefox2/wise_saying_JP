package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.local.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = DataStoreManager(application)

    private val _currentTheme = MutableStateFlow("gold")
    val currentTheme: StateFlow<String> = _currentTheme

    private val _notifyEnabled = MutableStateFlow(true)
    val notifyEnabled: StateFlow<Boolean> = _notifyEnabled

    private val _autoBlur = MutableStateFlow(false)
    val autoBlur: StateFlow<Boolean> = _autoBlur

    private val _lastUpdated = MutableStateFlow("")
    val lastUpdated: StateFlow<String> = _lastUpdated

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        dataStore.theme.onEach { _currentTheme.value = it }.launchIn(viewModelScope)
        dataStore.notifyEnabled.onEach { _notifyEnabled.value = it }.launchIn(viewModelScope)
        dataStore.autoBlur.onEach { _autoBlur.value = it }.launchIn(viewModelScope)
        dataStore.lastUpdate.onEach { _lastUpdated.value = it }.launchIn(viewModelScope)
    }

    fun setTheme(theme: String) {
        viewModelScope.launch { dataStore.setTheme(theme) }
    }

    fun toggleNotify() {
        viewModelScope.launch { dataStore.setNotify(!_notifyEnabled.value) }
    }

    fun toggleAutoBlur() {
        viewModelScope.launch { dataStore.setAutoBlur(!_autoBlur.value) }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // 로컬 데이터 새로고침 시뮬레이션
            kotlinx.coroutines.delay(1400)
            val now = java.time.LocalDateTime.now().toString().take(16)
            dataStore.setLastUpdate(now)
            _isRefreshing.value = false
        }
    }
}
