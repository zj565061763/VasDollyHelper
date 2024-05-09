package com.sd.app.vasdolly.model

import com.sd.app.vasdolly.moshi.fMoshi
import java.io.File

private val ConfigFile = File("ChannelConfig.json")
private val ConfigAdapter = fMoshi.adapter(ChannelConfig::class.java)

data class ChannelConfig(
    val channels: List<ChannelModel> = listOf(
        ChannelModel(
            name = "honor",
            selected = false,
        ),
        ChannelModel(
            name = "huawei",
            selected = false,
        ),
        ChannelModel(
            name = "oppo",
            selected = false,
        ),
        ChannelModel(
            name = "vivo",
            selected = false,
        ),
        ChannelModel(
            name = "xiaomi",
            selected = false,
        )
    ),
) {
    companion object {
        fun get(): ChannelConfig {
            return loadChannelConfig()
        }

        fun set(config: ChannelConfig) {
            val json = ConfigAdapter.toJson(config)
            ConfigFile.writeText(json)
        }
    }
}

private fun loadChannelConfig(): ChannelConfig {
    if (ConfigFile.isFile) {
        val json = ConfigFile.readText()
        if (json.isNotEmpty()) {
            return ConfigAdapter.fromJson(json)!!
        }
    }

    return ChannelConfig().also { config ->
        ConfigFile.deleteRecursively()
        ConfigFile.writeText(ConfigAdapter.toJson(config))
    }
}

data class ChannelModel(
    val name: String,
    val selected: Boolean,
)
