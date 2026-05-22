package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.local.DataStoreManager
import com.kotoba.takarabako.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = DataStoreManager(application)
    private val ctx = application.applicationContext

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

    private val _fontScale = MutableStateFlow(1.0f)
    val fontScale: StateFlow<Float> = _fontScale

    private val _notifyHour = MutableStateFlow(9)
    val notifyHour: StateFlow<Int> = _notifyHour

    private val _notifyMinute = MutableStateFlow(0)
    val notifyMinute: StateFlow<Int> = _notifyMinute

    init {
        dataStore.theme.onEach { _currentTheme.value = it }.launchIn(viewModelScope)
        dataStore.notifyEnabled.onEach { _notifyEnabled.value = it }.launchIn(viewModelScope)
        dataStore.autoBlur.onEach { _autoBlur.value = it }.launchIn(viewModelScope)
        dataStore.lastUpdate.onEach { _lastUpdated.value = it }.launchIn(viewModelScope)
        dataStore.fontScale.onEach { _fontScale.value = it }.launchIn(viewModelScope)
        dataStore.notifyHour.onEach { _notifyHour.value = it }.launchIn(viewModelScope)
        dataStore.notifyMinute.onEach { _notifyMinute.value = it }.launchIn(viewModelScope)
    }

    fun setTheme(theme: String) { viewModelScope.launch { dataStore.setTheme(theme) } }

    fun toggleNotify() {
        viewModelScope.launch {
            val next = !_notifyEnabled.value
            dataStore.setNotify(next)
            if (next) NotificationHelper.schedule(ctx, _notifyHour.value, _notifyMinute.value)
            else NotificationHelper.cancel(ctx)
        }
    }

    fun toggleAutoBlur() { viewModelScope.launch { dataStore.setAutoBlur(!_autoBlur.value) } }

    fun setFontScale(scale: Float) { viewModelScope.launch { dataStore.setFontScale(scale) } }

    fun setNotifyTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            dataStore.setNotifyHour(hour)
            dataStore.setNotifyMinute(minute)
            if (_notifyEnabled.value) NotificationHelper.schedule(ctx, hour, minute)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            kotlinx.coroutines.delay(1400)
            val now = java.time.LocalDateTime.now().toString().take(16)
            dataStore.setLastUpdate(now)
            _isRefreshing.value = false
        }
    }
}
