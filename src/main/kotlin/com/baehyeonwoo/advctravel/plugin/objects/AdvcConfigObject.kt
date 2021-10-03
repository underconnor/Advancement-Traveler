package com.baehyeonwoo.advctravel.plugin.objects

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

object AdvcConfigObject {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    val configFile = File(getInstance().dataFolder, "config.yml")
    val config = YamlConfiguration.loadConfiguration(configFile)
}