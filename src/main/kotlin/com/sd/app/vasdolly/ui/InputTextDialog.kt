package com.sd.app.vasdolly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun InputTextDialog(
    modifier: Modifier = Modifier,
    tips: String,
    button: String,
    onDismissRequest: () -> Unit,
    onClickButton: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        var value by remember { mutableStateOf("") }
        val requester = remember { FocusRequester() }

        LaunchedEffect(requester) {
            delay(100)
            requester.requestFocus()
        }

        Card(modifier = modifier) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.focusRequester(requester),
                    maxLines = 1,
                )

                if (tips.isNotEmpty()) {
                    Text(text = tips, fontSize = 14.sp, color = Color.Red)
                }

                Button(onClick = {
                    if (value.isNotEmpty()) {
                        onClickButton(value)
                    }
                }) {
                    Text(text = button)
                }
            }
        }
    }
}