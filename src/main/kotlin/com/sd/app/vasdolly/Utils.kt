package com.sd.app.vasdolly

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
fun loadResFileToPath(
    res: String,
    path: String,
): File {
    val file = File(path)
    if (file.isFile) return file

    file.deleteRecursively()
    ResourceLoader.Default.load(res).use {
        it.copyTo(file.outputStream())
    }
    return file
}

