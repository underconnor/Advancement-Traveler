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

package com.baehyeonwoo.advctravel.plugin.commands

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.title.Title.Times
import net.kyori.adventure.title.Title.title
import org.bukkit.plugin.Plugin
import java.time.Duration


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
                
                // TODO: FIX SWITCH
//                then("switch") {
//                    executes {
//                        sender.sendMessage("Usage: /advc switch <status/on/off>")
//                    }
//                    then("on") {
//                        executes {
//                            if (!enabled) {
//                                config.set("enabled", true)
//                                getInstance().saveConfig()
//                                server.pluginManager.registerEvents(AdvcTravelEvent(), getInstance())
//                                server.pluginManager.registerEvents(AdvcTpaEvent(), getInstance())
//                                AdvcTpaKommand.advcTpaKommand()
//                                sender.sendMessage(text("Advc is Now Enabled!", NamedTextColor.GREEN))
//                            }
//                            else {
//                                sender.sendMessage(text("Advc is Already Enabled.", NamedTextColor.RED))
//                            }
//                        }
//                    }
//                    then("off") {
//                        executes {
//                            if (enabled) {
//                                config.set("enabled", false)
//                                getInstance().saveConfig()
//                                HandlerList.unregisterAll(AdvcTravelEvent())
//                                // Not Unregistered AdvcTpaListener because of the Kommand.
//                                sender.sendMessage(text("Advc is Now Disabled!", NamedTextColor.GREEN))
//                            }
//                            else {
//                                sender.sendMessage(text("Advc is Already Disabled.", NamedTextColor.RED))
//                            }
//                        }
//                    }
//                    then("status") {
//                        executes {
//                            sender.sendMessage(text("Status: $enabled"))
//                        }
//                    }
//                }
                then("announce") {
                    then("title") {
                        then("title" to string(StringType.QUOTABLE_PHRASE)) {
                            then("subtitle") {
                                then("subtitle" to string(StringType.QUOTABLE_PHRASE)) {
                                    executes {
                                        val title: String by it
                                        val subtitle: String by it

                                        server.onlinePlayers.forEach { p ->
                                            p.showTitle(title(text(title), text(subtitle), Times.of(Duration.ofMillis(0L), Duration.ofMillis(7500L), Duration.ofMillis(0L))))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}