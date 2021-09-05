/*
 * Copyright (c) 2021 BaeHyeonWoo
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

import com.baehyeonwoo.advctravel.Listeners.FirstJoinEvent
import com.baehyeonwoo.advctravel.Listeners.JoinEvent
import com.baehyeonwoo.advctravel.Listeners.RespawnEvent
import org.bukkit.plugin.java.JavaPlugin

/***
 * @author BaeHyeonWoo
 */

class AdvcTravelMain : JavaPlugin() {

    companion object {
        lateinit var instance: AdvcTravelMain
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        server.maxPlayers = 999
//        server.maxPlayers = config.getInt("max-players")
        server.pluginManager.registerEvents(AdvcTravelEvent(), this)
        server.pluginManager.registerEvents(FirstJoinEvent(), this)
        server.pluginManager.registerEvents(RespawnEvent(), this)
        server.pluginManager.registerEvents(JoinEvent(), this)
        AdvcTravelKommand.advcTravelKommand()
    }
}