package com.baehyeonwoo.advctravel.plugin.tasks

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import com.baehyeonwoo.advctravel.plugin.objects.AdvcConfigObject
import org.bukkit.plugin.Plugin

class AdvcConfigReloadTask : Runnable {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val logger = getInstance().logger

    private val configFile = AdvcConfigObject.configFile

    private var configFileLastModified = configFile.lastModified()

    override fun run() {
        if (configFileLastModified != configFile.lastModified()) {
            val config = AdvcConfigObject.config

            AdvcConfigObject.config.load(AdvcConfigObject.configFile)
            AdvcConfigObject.config.save(AdvcConfigObject.configFile)

            configFileLastModified = configFile.lastModified()

            logger.info("Config reloaded.")
            logger.info("Config Administrator Settings: ${config.getString("administrator").toString()}")
            logger.info("Config Runner Settings: ${config.getString("runner").toString()}")
        }
    }
}