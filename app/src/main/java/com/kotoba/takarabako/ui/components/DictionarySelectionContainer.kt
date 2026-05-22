package com.kotoba.takarabako.ui.components

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

private class DictionaryTextToolbar(
    private val view: View,
    private val context: Context
) : TextToolbar {

    private var actionMode: ActionMode? = null

    override var status: TextToolbarStatus = TextToolbarStatus.Hidden
        private set

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        status = TextToolbarStatus.Shown
        actionMode?.finish()
        actionMode = view.startActionMode(object : ActionMode.Callback2() {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                if (onCopyRequested != null) {
                    menu.add(0, android.R.id.copy, 0, "복사")
                }
                if (onSelectAllRequested != null) {
                    menu.add(0, android.R.id.selectAll, 1, "전체 선택")
                }
                if (onCopyRequested != null) {
                    menu.add(0, MENU_ID_DICTIONARY, 2, "사전 검색")
                }
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    android.R.id.copy -> {
                        onCopyRequested?.invoke()
                        mode.finish()
                        true
                    }
                    android.R.id.selectAll -> {
                        onSelectAllRequested?.invoke()
                        true
                    }
                    MENU_ID_DICTIONARY -> {
                        onCopyRequested?.invoke()
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val query = clipboard.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
                        if (query.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                                putExtra(SearchManager.QUERY, query)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        }
                        mode.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                actionMode = null
                status = TextToolbarStatus.Hidden
            }
        }, ActionMode.TYPE_FLOATING)
    }

    override fun hide() {
        actionMode?.finish()
        actionMode = null
        status = TextToolbarStatus.Hidden
    }

    companion object {
        private const val MENU_ID_DICTIONARY = 1001
    }
}

@Composable
fun DictionarySelectionContainer(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    val toolbar = remember(view, context) { DictionaryTextToolbar(view, context) }
    CompositionLocalProvider(LocalTextToolbar provides toolbar) {
        SelectionContainer {
            content()
        }
    }
}
