package com.sd.app.vasdolly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.app.vasdolly.AppState

/**
 * 渠道
 */
@Composable
fun ChannelsView(
    modifier: Modifier = Modifier,
) {
    var showInputDialog by remember { mutableStateOf(false) }
    if (showInputDialog) {
        var tips by remember { mutableStateOf("") }
        InputTextDialog(
            button = "添加渠道",
            tips = tips,
            onDismissRequest = { showInputDialog = false },
            onClickButton = {
                if (AppState.addChannel(it)) {
                    tips = ""
                    showInputDialog = false
                } else {
                    tips = "渠道(${it})已存在"
                }
            }
        )
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SectionTitleView(
                title = "渠道",
                onClick = {
                    if (!AppState.isGenerating) {
                        showInputDialog = true
                    }
                },
            )
            LazyColumn(
                modifier = Modifier.widthIn(150.dp),
            ) {
                itemsIndexed(
                    items = AppState.channels,
                    key = { _, item -> item.name }
                ) { index, item ->
                    SectionItemView(
                        selected = item.selected,
                        name = item.name,
                        onClickRemove = { AppState.removeChannel(index) },
                        onClick = { AppState.clickChannel(index) },
                    )
                }
            }
        }
    }
}