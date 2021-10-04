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
import com.baehyeonwoo.advctravel.plugin.events.AdvcBanItemEvent
import com.baehyeonwoo.advctravel.plugin.events.AdvcTpaEvent
import com.baehyeonwoo.advctravel.plugin.events.AdvcTravelEvent
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title.Times
import net.kyori.adventure.title.Title.title
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import java.time.Duration


/***
 * @author BaeHyeonWoo
 */

object AdvcTravelKommand {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private fun getConfig(): FileConfiguration{
        return getInstance().config
    }

    private val server = getInstance().server

    fun advcTravelKommand() {
        getInstance().kommand {
            register("advc") {
                then("administrator") {
                    executes {
                        sender.sendMessage(text("Current Administrator UUID settings: ${requireNotNull(getConfig().getString("administrator").toString())}"))
                    }
                }
                then("runner") {
                    executes {
                        sender.sendMessage(text("Current Runner UUID settings: ${requireNotNull(getConfig().getString("runner").toString())}"))
                    }
                }
                then("switch") {
                    then("status") {
                        executes {
                            sender.sendMessage(text(getConfig().getString("enabled").toString()))
                        }
                    }
                    then("on") {
                        executes {
                            val enabled = getConfig().getBoolean("enabled")
                            if (!enabled){
                                getConfig().set("enabled", true)
                                server.pluginManager.registerEvents(AdvcTravelEvent(), getInstance())
                                server.pluginManager.registerEvents(AdvcBanItemEvent(), getInstance())
                                sender.sendMessage(text("Advc is Now Enabled!", NamedTextColor.GREEN))
                                getInstance().saveConfig()
                                getInstance().reloadConfig()
                            }
                            else {
                                sender.sendMessage(text("Advc is Already Enabled.", NamedTextColor.RED))
                            }
                        }
                    }
                    then("off") {
                        executes {
                            val enabled = getConfig().getBoolean("enabled")
                            if (enabled) {
                                getConfig().set("enabled", false)
                                HandlerList.unregisterAll()
                                server.pluginManager.registerEvents(AdvcTpaEvent(), getInstance())
                                getInstance().saveConfig()
                                getInstance().reloadConfig()
                                sender.sendMessage(text("Advc is Now Disabled!", NamedTextColor.GREEN))
                            }
                            else {
                                sender.sendMessage(text("Advc is Already Disabled.", NamedTextColor.RED))
                            }
                        }
                    }
                }
                then("announce") {
                    then("times") {
                        then("fadein" to int()) {
                            then("stay" to int()) {
                                then("fadeout" to int()) {
                                    executes {
                                        val fadein: Int by it
                                        val stay: Int by it
                                        val fadeout: Int by it

                                        getConfig().set("fadein", fadein)
                                        getConfig().set("stay", stay)
                                        getConfig().set("fadeout", fadeout)
                                        sender.sendMessage(text("Title ticks has been set to:\n" +
                                                "Fade In: $fadein\n" +
                                                "Stay: $stay\n" +
                                                "Fade Out: $fadeout"))
                                        getInstance().saveConfig()
                                    }
                                }
                            }
                        }
                    }
                    then("title") {
                        then("title" to string(StringType.QUOTABLE_PHRASE)) {
                            then("subtitle") {
                                then("subtitle" to string(StringType.QUOTABLE_PHRASE)) {
                                    executes {
                                        val title: String by it
                                        val subtitle: String by it
                                        val fadein = getConfig().getInt("fadein")
                                        val stay = getConfig().getInt("stay")
                                        val fadeout = getConfig().getInt("fadeout")

                                        server.onlinePlayers.forEach { p ->
                                            p.showTitle(title(text(title), text(subtitle),
                                                Times.of(Duration.ofMillis((fadein / 20).toLong()), Duration.ofMillis((stay / 20).toLong()), Duration.ofMillis((fadeout / 20).toLong()))))
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