package com.baehyeonwoo.advctravel.plugin.tasks

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import com.baehyeonwoo.advctravel.plugin.objects.AdvcWhitelist
import com.google.common.collect.ImmutableSortedSet
import org.bukkit.plugin.Plugin
import java.io.File

class AdvcConfigReloadTask : Runnable {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private lateinit var allows: Set<String>

    private val logger = getInstance().logger

    private val configFile = File(getInstance().dataFolder, "config.yml")

    private val whitelistFile = File(getInstance().dataFolder, "Whitelist.txt")

    private var configFileLastModified = configFile.lastModified()

    private var whitelistFileLastModified = whitelistFile.lastModified()

    override fun run() {
        if (configFileLastModified != configFile.lastModified()) {
            getInstance().reloadConfig()
            getInstance().saveConfig()

            configFileLastModified = configFile.lastModified()

//            logger.info("Config reloaded.")
//            logger.info("Config Administrator Settings: ${config.getString("administrator").toString()}")
//            logger.info("Config Runner Settings: ${config.getString("runner").toString()}")
        }

        if (whitelistFileLastModified != whitelistFile.lastModified()) {
            whitelistFileLastModified = whitelistFile.lastModified()

            val lines = whitelistFile.readLines()
            allows = ImmutableSortedSet.copyOf(String.CASE_INSENSITIVE_ORDER, lines)
            AdvcWhitelist.allows = ImmutableSortedSet.copyOf(String.CASE_INSENSITIVE_ORDER, lines)
            logger.info("Whitelist File reloaded.")
            logger.info("Current Whitelist: $allows")
        }
    }
}
