package com.sd.app.vasdolly.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.FrameWindowScope
import java.awt.FileDialog
import java.io.File

@Composable
fun FrameWindowScope.SelectApkDialog(
    title: String,
    onResult: (result: File?) -> Unit,
) = AwtWindow(
    create = {
        object : FileDialog(window, title) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    if (file != null) {
                        onResult(File(directory).resolve(file))
                    } else {
                        onResult(null)
                    }
                }
            }
        }.apply {
            this.setFilenameFilter { _, name ->
                name.endsWith(".apk")
            }
        }
    },
    dispose = FileDialog::dispose
)