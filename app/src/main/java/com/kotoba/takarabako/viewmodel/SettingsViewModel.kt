package com.kotoba.takarabako.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotoba.takarabako.data.local.DataStoreManager
import com.kotoba.takarabako.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = DataStoreManager.getInstance(application)
    private val ctx = application.applicationContext

    private val _currentTheme = MutableStateFlow("gold")
    val currentTheme: StateFlow<String> = _currentTheme

    private val _notifyEnabled = MutableStateFlow(false)
    val notifyEnabled: StateFlow<Boolean> = _notifyEnabled

    private val _autoBlur = MutableStateFlow(false)
    val autoBlur: StateFlow<Boolean> = _autoBlur

    private val _fontScale = MutableStateFlow(1.0f)
    val fontScale: StateFlow<Float> = _fontScale

    private val _notifyHour = MutableStateFlow(9)
    val notifyHour: StateFlow<Int> = _notifyHour

    private val _notifyMinute = MutableStateFlow(0)
    val notifyMinute: StateFlow<Int> = _notifyMinute

    private val _autoPlay = MutableStateFlow(false)
    val autoPlay: StateFlow<Boolean> = _autoPlay

    private val _autoBlurDelay = MutableStateFlow(3)
    val autoBlurDelay: StateFlow<Int> = _autoBlurDelay

    private val _loginStreak = MutableStateFlow(1)
    val loginStreak: StateFlow<Int> = _loginStreak

    init {
        dataStore.theme.onEach { _currentTheme.value = it }.launchIn(viewModelScope)
        dataStore.notifyEnabled.onEach { _notifyEnabled.value = it }.launchIn(viewModelScope)
        dataStore.autoBlur.onEach { _autoBlur.value = it }.launchIn(viewModelScope)
        dataStore.fontScale.onEach { _fontScale.value = it }.launchIn(viewModelScope)
        dataStore.notifyHour.onEach { _notifyHour.value = it }.launchIn(viewModelScope)
        dataStore.notifyMinute.onEach { _notifyMinute.value = it }.launchIn(viewModelScope)
        dataStore.autoPlay.onEach { _autoPlay.value = it }.launchIn(viewModelScope)
        dataStore.autoBlurDelay.onEach { _autoBlurDelay.value = it }.launchIn(viewModelScope)
        dataStore.loginStreak.onEach { _loginStreak.value = it }.launchIn(viewModelScope)
        updateLoginStreak()
    }

    private fun updateLoginStreak() {
        viewModelScope.launch {
            val today = java.time.LocalDate.now().toString()
            val lastDate = dataStore.lastLoginDate.first()
            val currentStreak = dataStore.loginStreak.first()
            val yesterday = java.time.LocalDate.now().minusDays(1).toString()
            val newStreak = when {
                lastDate == today -> currentStreak
                lastDate == yesterday -> currentStreak + 1
                else -> 1
            }
            dataStore.setLastLoginDate(today)
            dataStore.setLoginStreak(newStreak)
        }
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

    fun toggleAutoPlay() { viewModelScope.launch { dataStore.setAutoPlay(!_autoPlay.value) } }

    fun setAutoBlurDelay(seconds: Int) { viewModelScope.launch { dataStore.setAutoBlurDelay(seconds) } }

    fun setFontScale(scale: Float) { viewModelScope.launch { dataStore.setFontScale(scale) } }

    fun setNotifyTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            dataStore.setNotifyTime(hour, minute)
            if (_notifyEnabled.value) NotificationHelper.schedule(ctx, hour, minute)
        }
    }
}
