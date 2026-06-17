package com.kotoba.takarabako.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.withTimeoutOrNull

fun Modifier.cardSwipe(key: Any, onPrev: () -> Unit, onNext: () -> Unit): Modifier =
    this.pointerInput(key) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val up = withTimeoutOrNull(200L) { waitForUpOrCancellation() }
            if (up != null) {
                if (down.position.x < size.width / 2f) onPrev() else onNext()
            }
        }
    }
