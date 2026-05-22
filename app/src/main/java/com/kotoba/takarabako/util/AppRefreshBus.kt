package com.kotoba.takarabako.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AppRefreshBus {
    private var refreshCount = 0
    private val _tick = MutableStateFlow(0)
    val tick: StateFlow<Int> = _tick

    fun refresh() {
        refreshCount++
        _tick.value = refreshCount
    }
}
