package com.kotoba.takarabako.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AppRefreshBus {
    private val _tick = MutableStateFlow(0)
    val tick: StateFlow<Int> = _tick

    fun refresh() { _tick.value++ }
}
