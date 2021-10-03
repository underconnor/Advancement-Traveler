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
import com.baehyeonwoo.advctravel.plugin.events.AdvcTravelEvent
import com.baehyeonwoo.advctravel.plugin.objects.AdvcConfigObject
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title.Times
import net.kyori.adventure.title.Title.title
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

    private val server = getInstance().server

    fun advcTravelKommand() {
        getInstance().kommand {
            register("advc") {
                val config = AdvcConfigObject.config
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
                then("switch") {
                    then("status") {
                        executes {
                            sender.sendMessage(text(config.getString("enabled").toString()))
                        }
                    }
                    then("on") {
                        executes {
                            val enabled = config.getBoolean("enabled")
                            if (!enabled){
                                config.set("enabled", true)
                                server.pluginManager.registerEvents(AdvcTravelEvent(), getInstance())
                                server.pluginManager.registerEvents(AdvcBanItemEvent(), getInstance())
                                sender.sendMessage(text("Advc is Now Enabled!", NamedTextColor.GREEN))
                                AdvcConfigObject.config.load(AdvcConfigObject.configFile)
                                AdvcConfigObject.config.save(AdvcConfigObject.configFile)
                            }
                            else {
                                sender.sendMessage(text("Advc is Already Enabled.", NamedTextColor.RED))
                            }
                        }
                    }
                    then("off") {
                        executes {
                            val enabled = config.getBoolean("enabled")
                            if (enabled) {
                                config.set("enabled", false)
                                HandlerList.unregisterAll(AdvcTravelEvent())
                                HandlerList.unregisterAll(AdvcBanItemEvent())
                                AdvcConfigObject.config.load(AdvcConfigObject.configFile)
                                AdvcConfigObject.config.save(AdvcConfigObject.configFile)
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

                                        config.set("fadein", fadein)
                                        config.set("fadein", stay)
                                        config.set("fadein", fadeout)
                                        sender.sendMessage(text("Title ticks has been set to:\n" +
                                                "Fade In: $fadein\n" +
                                                "Stay: $stay\n" +
                                                "Fade Out: $fadeout"))
                                        AdvcConfigObject.config.load(AdvcConfigObject.configFile)
                                        AdvcConfigObject.config.save(AdvcConfigObject.configFile)
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
                                        val fadein = config.getInt("fadein")
                                        val stay = config.getInt("stay")
                                        val fadeout = config.getInt("fadeout")

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