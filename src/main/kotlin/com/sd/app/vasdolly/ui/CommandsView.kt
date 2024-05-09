package com.sd.app.vasdolly.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sd.app.vasdolly.AppState

/**
 * 命令行
 */
@Composable
fun CommandsView(
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    val commandLines by AppState.commandLinesFlow.collectAsState()

    val totalItemsCount = state.layoutInfo.totalItemsCount
    LaunchedEffect(state, totalItemsCount) {
        state.scrollToItem(totalItemsCount)
    }

    Card(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(10.dp),
        ) {
            items(commandLines) { line ->
                Text(text = line, lineHeight = 1.em)
            }
        }
    }
}