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

import io.github.monun.kommand.KommandSource
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.plugin.Plugin


/***
 * @author BaeHyeonWoo
 */

object AdvcTravelKommand {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server
    
    private val config = getInstance().config

    fun advcTravelKommand() {
        getInstance().kommand {
            register("advc") {
                then("administrator") {
                    executes {
                        sender.sendMessage(text("Current Administrator UUID settings: ${requireNotNull(config.getString("administrator").toString())}"))
                    }
                }
                then("runner") {
                    executes {
                        sender.sendMessage(text("Current Runner UUID settings: ${requireNotNull(config.getString("runner").toString())}"))
                    }
                }
                then ("maxPlayers") {
                    executes {
                        sender.sendMessage(text("Current maxPlayer settings: ${server.maxPlayers}"))
                        sender.sendMessage(text("Current Config maxPlayer settings: ${requireNotNull(config.getInt("maxplayers"))}"))
                    }
                    then("newMaxPlayer" to int()) {
                        executes {
                            newMaxPlayers(it["newMaxPlayer"])
                        }
                    }
                }
                then("reload") {
                    executes {
                        getInstance().reloadConfig()
                        sender.sendMessage(text("Config Reloaded."))
                    }
                }
            }
        }
    }
    private fun KommandSource.newMaxPlayers(maxPlayers: Int) {
        config.set("maxplayers", maxPlayers)
        getInstance().saveConfig()
        server.maxPlayers = maxPlayers
        AdvcTravelEvent().maxPlayers = maxPlayers
        feedback(text("maxPlayers = $maxPlayers"))
    }
}