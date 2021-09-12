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

import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


/***
 * @author BaeHyeonWoo
 */

object AdvcTravelKommand {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }
    
    private val config = getInstance().config

    private val server = getInstance().server

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
            }
//            register("tpa") {
//                requires { playerOrNull != null }
//                executes {
//                    sender.sendMessage(text("사용법: /tpa [PlayerName]", NamedTextColor.RED))
//                }
//                then("requestPlayer" to player()) {
//                    executes {
//                        val requestPlayer: String by it
//                        val tpaPlayer = requireNotNull(server.getPlayer(requestPlayer))
//
//                        player.sendMessage(text("${tpaPlayer.name}에게 텔레포트 요청을 보냈습니다.\n"))
//
//                    }
//                }
//            }
//            register("tpahere") {
//                requires { playerOrNull != null }
//                executes {
//                    sender.sendMessage(text("사용법: /tpahere [PlayerName]", NamedTextColor.RED))
//                }
//                then("requestPlayer" to player()) {
//
//                }
//            }
//            register("tpaccept") {
//
//            }
//            register("tpac") {
//
//            }
//            register("tpdeny") {
//
//            }
//            register("tpd") {
//
//            }
//            register("tpacancel") {
//
//            }
//            register("tpaca") {
//
//            }
        }
    }
}