/*
 * Copyright (c) 2021 BaeHyeonWoo & Others
 *
 *  Licensed under the General Public License, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/gpl-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baehyeonwoo.advctravel

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/***
 * @author BaeHyeonWoo
 *
 * @Co_author FSanchir & PatrickKR
 */

class AdvcTravelMain : JavaPlugin() {

    companion object {
        lateinit var instance: AdvcTravelMain
            private set
    }

    private val configFile = File(dataFolder, "config.yml")

    override fun onEnable() {
        instance = this

        AdvcTravelConfig.load(configFile)
        logger.info("Config Administrator Settings: ${config.getString("administrator")}")
        logger.info("Config Runner Settings: ${config.getString("runner")}")
        server.maxPlayers = config.getInt("maxplayers")
        server.pluginManager.registerEvents(AdvcTravelEvent(), this)
        AdvcTravelKommand.advcTravelKommand()

        val sm = Bukkit.getScoreboardManager()
        val sc = sm.mainScoreboard

        val hunter = sc.getTeam("Hunter")
        if(hunter == null) sc.registerNewTeam("Hunter")

        hunter?.setAllowFriendlyFire(false)
        hunter?.setCanSeeFriendlyInvisibles(false)
        hunter?.color(NamedTextColor.RED)

        val runner = sc.getTeam("Runner")
        if(runner == null) sc.registerNewTeam("Runner")

        hunter?.setAllowFriendlyFire(false)
        hunter?.setCanSeeFriendlyInvisibles(false)
        hunter?.color(NamedTextColor.AQUA)
    }

    override fun onDisable() {
        config.set("maxplayers", server.maxPlayers)
        saveConfig()
    }
}