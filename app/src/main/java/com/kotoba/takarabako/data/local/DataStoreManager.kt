package com.kotoba.takarabako.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
        val LAST_UPDATE = stringPreferencesKey("last_update")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[THEME_KEY] ?: "gold" }
    val notifyEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIFY_KEY] ?: true }
    val autoBlur: Flow<Boolean> = context.dataStore.data.map { it[AUTOBLUR_KEY] ?: false }
    val lastUpdate: Flow<String> = context.dataStore.data.map { it[LAST_UPDATE] ?: "" }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME_KEY] = theme }
    }

    suspend fun setNotify(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFY_KEY] = enabled }
    }

    suspend fun setAutoBlur(enabled: Boolean) {
        context.dataStore.edit { it[AUTOBLUR_KEY] = enabled }
    }

    suspend fun setLastUpdate(date: String) {
        context.dataStore.edit { it[LAST_UPDATE] = date }
    }
}
