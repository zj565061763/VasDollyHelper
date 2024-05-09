package com.sd.app.vasdolly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.sd.app.vasdolly.AppState

@Composable
fun TipsDialogView(
    modifier: Modifier = Modifier,
) {
    if (AppState.tips.isNotEmpty()) {
        Dialog(onDismissRequest = { AppState.clearTips() }) {
            Card(modifier = modifier) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = AppState.tips)
                }
            }
        }
    }
}