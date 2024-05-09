package com.sd.app.vasdolly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.onExternalDrag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sd.app.vasdolly.ui.ChannelsView
import com.sd.app.vasdolly.ui.CommandsView
import com.sd.app.vasdolly.ui.SelectApkDialog
import com.sd.app.vasdolly.ui.SelectApkView
import com.sd.app.vasdolly.ui.TipsDialogView

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    singleWindowApplication(
        title = "分包助手",
        state = WindowState(
            width = 1000.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        ),
    ) {
        TipsDialogView()
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().padding(10.dp)
                    .onExternalDrag { AppState.onDragDrop(it) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SelectApkView()
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    ChannelsView()
                    Spacer(modifier = Modifier.width(10.dp))
                    CommandsView(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                BottomView()
            }
        }

        if (AppState.selectApkDialog.isAwaiting) {
            SelectApkDialog(
                title = "选择Apk",
                onResult = {
                    AppState.selectApkDialog.onResult(it)
                }
            )
        }
    }
}

@Composable
private fun BottomView(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { AppState.start() },
            enabled = AppState.apkFile != null && AppState.channels.isNotEmpty(),
            modifier = Modifier.defaultMinSize(128.dp, 48.dp)
        ) {
            if (AppState.isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text = "开始分包")
            }
        }
    }
}
