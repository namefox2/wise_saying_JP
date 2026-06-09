package com.kotoba.takarabako.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kotoba_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val NOTIFY_KEY = booleanPreferencesKey("notify")
        val AUTOBLUR_KEY = booleanPreferencesKey("auto_blur")
        val FONT_SCALE_KEY = floatPreferencesKey("font_scale")
        val NOTIFY_HOUR_KEY = intPreferencesKey("notify_hour")
        val NOTIFY_MINUTE_KEY = intPreferencesKey("notify_minute")
        val AUTO_PLAY_KEY = booleanPreferencesKey("auto_play")
        val STREAK_KEY = intPreferencesKey("login_streak")
        val LAST_LOGIN_DATE_KEY = stringPreferencesKey("last_login_date")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[THEME_KEY] ?: "gold" }
    val notifyEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIFY_KEY] ?: true }
    val autoBlur: Flow<Boolean> = context.dataStore.data.map { it[AUTOBLUR_KEY] ?: false }
    val fontScale: Flow<Float> = context.dataStore.data.map { it[FONT_SCALE_KEY] ?: 1.0f }
    val notifyHour: Flow<Int> = context.dataStore.data.map { it[NOTIFY_HOUR_KEY] ?: 9 }
    val notifyMinute: Flow<Int> = context.dataStore.data.map { it[NOTIFY_MINUTE_KEY] ?: 0 }
    val autoPlay: Flow<Boolean> = context.dataStore.data.map { it[AUTO_PLAY_KEY] ?: false }
    val loginStreak: Flow<Int> = context.dataStore.data.map { it[STREAK_KEY] ?: 1 }
    val lastLoginDate: Flow<String> = context.dataStore.data.map { it[LAST_LOGIN_DATE_KEY] ?: "" }

    suspend fun setTheme(theme: String) { context.dataStore.edit { it[THEME_KEY] = theme } }
    suspend fun setNotify(enabled: Boolean) { context.dataStore.edit { it[NOTIFY_KEY] = enabled } }
    suspend fun setAutoBlur(enabled: Boolean) { context.dataStore.edit { it[AUTOBLUR_KEY] = enabled } }
    suspend fun setFontScale(scale: Float) { context.dataStore.edit { it[FONT_SCALE_KEY] = scale } }
    suspend fun setNotifyHour(hour: Int) { context.dataStore.edit { it[NOTIFY_HOUR_KEY] = hour } }
    suspend fun setNotifyMinute(minute: Int) { context.dataStore.edit { it[NOTIFY_MINUTE_KEY] = minute } }
    suspend fun setAutoPlay(enabled: Boolean) { context.dataStore.edit { it[AUTO_PLAY_KEY] = enabled } }
    suspend fun setLoginStreak(streak: Int) { context.dataStore.edit { it[STREAK_KEY] = streak } }
    suspend fun setLastLoginDate(date: String) { context.dataStore.edit { it[LAST_LOGIN_DATE_KEY] = date } }
}
