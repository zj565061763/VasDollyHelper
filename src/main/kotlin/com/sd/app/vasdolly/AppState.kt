package com.sd.app.vasdolly

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.DragData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ExternalDragValue
import com.sd.app.vasdolly.model.ChannelConfig
import com.sd.app.vasdolly.model.ChannelModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections

@OptIn(FlowPreview::class)
object AppState {
    private val _scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /** Apk */
    var apkFile by mutableStateOf<File?>(null)
        private set

    /** 渠道 */
    val channels = mutableStateListOf<ChannelModel>()

    private val _commandLines: MutableList<String> = Collections.synchronizedList(mutableListOf())
    private val _commandLinesFlow = MutableStateFlow<List<String>>(emptyList())
    val commandLinesFlow: StateFlow<List<String>> = _commandLinesFlow.asStateFlow()

    /** 是否正在生成中 */
    var isGenerating by mutableStateOf(false)
        private set

    /** 提示文字 */
    var tips by mutableStateOf("")
        private set

    /** 选择Apk窗口 */
    val selectApkDialog = DialogState<File?>()

    /**
     * 打开Apk选择路径
     */
    fun selectApk() {
        if (isGenerating) return
        _scope.launch {
            apkFile = selectApkDialog.awaitResult()
        }
    }

    /**
     * 点击渠道
     */
    fun clickChannel(index: Int) {
        if (isGenerating) return
        if (index in channels.indices) {
            val channel = channels[index]
            channels[index] = channel.copy(selected = !channel.selected)
        }
    }

    /**
     * 删除渠道
     */
    fun removeChannel(index: Int) {
        if (isGenerating) return
        if (index in channels.indices) {
            channels.removeAt(index)
        }
    }

    /**
     * 添加渠道
     */
    fun addChannel(channel: String): Boolean {
        if (channels.find { it.name == channel } != null) {
            return false
        }
        channels.add(
            ChannelModel(
                name = channel,
                selected = false,
            )
        )
        channels.sortBy { it.name }
        return true
    }

    /**
     * 拖动Apk
     */
    @OptIn(ExperimentalComposeUiApi::class)
    fun onDragDrop(value: ExternalDragValue) {
        if (isGenerating) return

        val data = value.dragData
        if (data !is DragData.FilesList) return

        val files = data.readFiles()
        if (files.size > 1) return

        val first = files.first()
        if (!first.endsWith(".apk")) return

        val path = first.takeIf { !it.startsWith("file:/") } ?: first.removePrefix("file:")
        apkFile = File(path)
    }

    /**
     * 清除提示
     */
    fun clearTips() {
        this.tips = ""
    }

    /**
     * 开始生成
     */
    fun start() {
        _scope.launch {
            generate()
        }
    }

    private suspend fun generate() {
        if (isGenerating) return

        val apkFile = apkFile ?: return
        if (!apkFile.exists()) {
            this.apkFile = null
            return
        }

        val output = File("apks")
        if (!output.exists()) {
            if (!output.mkdirs()) {
                tips = "创建apks目录失败"
                return
            }
        }

        val channelString = channels.asSequence()
            .filter { it.selected }
            .map { it.name }
            .joinToString(",")

        if (channelString.isEmpty()) {
            tips = "渠道信息为空"
            return
        }

        val command = "java -jar VasDolly.jar put -c $channelString $apkFile $output"

        try {
            isGenerating = true
            withContext(Dispatchers.IO) {
                loadResFileToPath(
                    res = "files/VasDolly.jar",
                    path = "VasDolly.jar",
                )
                clearCommandLines()
                executeCommand(command) { addCommandLine(it) }
            }
        } catch (e: Throwable) {
            tips = e.toString()
        } finally {
            isGenerating = false
        }
    }

    /**
     * 添加命令行
     */
    private fun addCommandLine(line: String) {
        _commandLines.add(line)
        _commandLinesFlow.value = _commandLines.toList()
    }

    /**
     * 清空命令行
     */
    private fun clearCommandLines() {
        _commandLines.clear()
        _commandLinesFlow.value = emptyList()
    }

    init {
        this.channels.addAll(
            ChannelConfig.get().channels.sortedBy { it.name }
        )

        _scope.launch {
            snapshotFlow { apkFile }
                .distinctUntilChanged()
                .collect {
                    clearCommandLines()
                }
        }

        _scope.launch {
            snapshotFlow { channels.toList() }
                .debounce(500)
                .collect { list ->
                    ChannelConfig.set(ChannelConfig(channels = list))
                }
        }
    }
}

private fun executeCommand(
    command: String,
    onLine: (String) -> Unit,
) {
    val process = Runtime.getRuntime().exec(command)
    val reader = process.inputStream.bufferedReader()
    while (true) {
        val line = reader.readLine() ?: break
        onLine(line)
    }
}

class DialogState<T> {
    private var onResult: CompletableDeferred<T>? by mutableStateOf(null)

    val isAwaiting get() = onResult != null

    suspend fun awaitResult(): T {
        onResult = CompletableDeferred()
        val result = onResult!!.await()
        onResult = null
        return result
    }

    fun onResult(result: T) = onResult!!.complete(result)
}