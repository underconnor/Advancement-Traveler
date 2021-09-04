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
import io.github.monun.kommand.StringType
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

    private val config = getInstance().config

    fun advcTravelKommand() {
        getInstance().kommand {
            register("advc") {
                requires { playerOrNull != null }
                then("runner") {
                    executes {
                        player.sendMessage(text("Current Runner UUID settings: ${config.getString("runner")}"))
                    }
                    then("runnerUUID" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            runner(it["runnerUUID"])
                        }
                    }
                }
                then ("maxPlayers") {
                    executes {
                        player.sendMessage(text("Current maxPlayer settings: ${getInstance().server.maxPlayers}"))
                        player.sendMessage(text("Config maxPlayer settings: ${config.getInt("max-players")}"))
                    }
                    then ("newMaxPlayers" to int()) {
                        executes {
                            maxPlayer(it["newMaxPlayers"])
                        }
                    }
                }
                then("administrator") {
                    executes {
                        player.sendMessage(text("Current Administrator UUID settings: ${config.getString("administrator")}"))
                    }
                    then("administratorUUID") {
                        executes {
                            administrator(it["administratorUUID"])
                        }
                    }
                }
            }
        }
    }

    private fun KommandSource.runner(runnerUUID: String) {
        config.set("runner", runnerUUID)
        getInstance().saveConfig()
        feedback(text("Current Player UUID settings has been changed to: \"$runnerUUID\"."))
    }
    private fun KommandSource.administrator(administrator: String) {
        config.set("administrator", administrator)
        getInstance().saveConfig()
        feedback(text("Current Administrator UUID settings has been changed to: \"$administrator\"."))
    }
    private fun KommandSource.maxPlayer(maxPlayers: Int) {
        getInstance().server.maxPlayers = maxPlayers
        config.set("max-players", maxPlayers)
        getInstance().saveConfig()
        feedback(text("Current maxPlayers settings has been changed to: \"$maxPlayers\"."))
    }
}