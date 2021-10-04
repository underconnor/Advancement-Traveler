package com.baehyeonwoo.advctravel.plugin.tasks

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import org.bukkit.plugin.Plugin
import java.io.File

class AdvcConfigReloadTask : Runnable {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val logger = getInstance().logger

    private val configFile = File(getInstance().dataFolder, "config.yml")

    private var configFileLastModified = configFile.lastModified()

    override fun run() {
        if (configFileLastModified != configFile.lastModified()) {
            getInstance().reloadConfig()
            getInstance().saveConfig()

            configFileLastModified = configFile.lastModified()

            val config = getInstance().config

            logger.info("Config reloaded.")
            logger.info("Config Administrator Settings: ${config.getString("administrator").toString()}")
            logger.info("Config Runner Settings: ${config.getString("runner").toString()}")
        }
    }
}