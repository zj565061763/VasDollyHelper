package com.sd.app.vasdolly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.app.vasdolly.AppState

/**
 * 选择Apk
 */
@Composable
fun SelectApkView(
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { AppState.selectApk() },
                enabled = !AppState.isGenerating,
            ) {
                Text(text = "选择Apk")
            }

            Box(modifier = Modifier.heightIn(50.dp)) {
                AppState.apkFile?.let { path ->
                    SelectionContainer {
                        Text(path.toString())
                    }
                }
            }
        }
    }
}