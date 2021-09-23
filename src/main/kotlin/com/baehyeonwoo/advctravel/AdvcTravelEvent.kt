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

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.*
import org.bukkit.event.player.PlayerLoginEvent.Result
import org.bukkit.plugin.Plugin
import kotlin.random.Random


/***
 * @author BaeHyeonWoo
 *
 * @Co_author FSanchir & PatrickKR
 */

class AdvcTravelEvent : Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val config = getInstance().config

    private val administrator = requireNotNull(config.getString("administrator").toString())

    private val runner = requireNotNull(config.getString("runner").toString())

    private fun randomTeleport(player: Player) {
        val randomX = Random.nextDouble(-1000.0, 1000.0)
        val randomZ = Random.nextDouble(-1000.0, 1000.0)

        player.teleport(player.location.set(randomX, player.world.getHighestBlockYAt(randomX.toInt(), randomZ.toInt()).toDouble(), randomZ))
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val p = e.player
        p.noDamageTicks = 0

        val sm = Bukkit.getScoreboardManager()
        val sc = sm.mainScoreboard
        if(runner.contains(p.uniqueId.toString())){
            val runner = sc.getTeam("Runner")
            runner?.addEntry(p.name)
        } else{
            val hunter = sc.getTeam("Hunter")
            hunter?.addEntry(p.name)
        }

        if (!p.hasPlayedBefore()) {
            server.scheduler.runTaskLater(getInstance(), Runnable {
                randomTeleport(p)
            }, 0)
        }
    }

    @EventHandler
    fun onPlayerAdvancementDone(e: PlayerAdvancementDoneEvent) {
        val p = e.player
        val advancement = e.advancement

        if (p.uniqueId.toString() in runner) {
            if (!advancement.key.toString().startsWith("minecraft:recipes") && !advancement.key.toString().endsWith("root")) {
                ++server.maxPlayers
                config.set("maxplayers", server.maxPlayers)
                getInstance().saveConfig()
            }
        }
    }

    @EventHandler
    fun onAsyncChat(e: AsyncChatEvent) {
        val p = e.player
        val msgComponent = e.message()
        val msg = (msgComponent as TextComponent).content()

        if (p.uniqueId.toString() in administrator) {
            e.isCancelled = false
        }
        else {
            server.scheduler.runTask(
                getInstance(),
                Runnable {
                    server.dispatchCommand(p as CommandSender, "teammsg $msg")
                }
            )

            e.isCancelled = true
            Bukkit.getConsoleSender().sendMessage(text("${p.name} issued server command: /teammsg $msg"))
        }
    }

    @EventHandler
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        val c = e.message

        if(c.startsWith("/tpa") || c.startsWith("/teammsg") || c.startsWith("/tm")) e.isCancelled = false
        else e.isCancelled = p.uniqueId.toString() !in administrator
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        val player = e.player
        val block = e.block

        if (Tag.BEDS.isTagged(block.type) && player.world.environment != World.Environment.NORMAL) {
            e.isCancelled = true
            player.sendMessage(text("지옥과 엔더에서는 침대가 막혀있습니다!", NamedTextColor.RED))
        }
        if (block.type == Material.RESPAWN_ANCHOR && player.world.environment != World.Environment.NETHER) {
            e.isCancelled = true
            player.sendMessage(text("오버월드와 엔더에서는 리스폰 정박기가 막혀있습니다!", NamedTextColor.RED))
        }
        if (block.type == Material.PISTON || block.type == Material.STICKY_PISTON || block.type == Material.HOPPER) {
            e.isCancelled = true
            player.sendMessage(text("NO.", NamedTextColor.RED))
        }
    }

    /* (Team으로 해결)
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.entity is Player) {
            var player: Player? = null
            when (val damager = e.damager) {
                is Player -> {
                    player = damager
                }
//                is Tameable -> {
//                    if (damager is Wolf) {
//                        if (!runner.contains(damager.target?.uniqueId.toString())) {
//                            damager.isAngry = false
//                        }
//
//                    }
//
//                    player = damager.owner as Player
//                }
                is Projectile -> {
                    if (damager.shooter is Player) {
                        player = damager.shooter as Player
                    }
                }
            }
            if ((player != null) && !runner.contains(player.uniqueId.toString()) && !runner.contains(e.entity.uniqueId.toString())) {
                e.isCancelled = true
            }
        }
    }
    */

    @EventHandler
    fun onPlayerLogin(e: PlayerLoginEvent) {
        val p = e.player
        if(p.uniqueId.toString() in administrator || p.uniqueId.toString() in runner) {
            if(!p.isBanned) {
                if(e.result == Result.KICK_FULL) {
                    e.allow()
                }
                ++server.maxPlayers
            }
        }
        config.set("maxplayers", server.maxPlayers)
        getInstance().saveConfig()
    }


    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val p = e.player

        if(p.uniqueId.toString() in administrator || p.uniqueId.toString() in runner) {
            --server.maxPlayers
        }

        config.set("maxplayers", server.maxPlayers)
        getInstance().saveConfig()
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        val p = e.player

        if (p.bedSpawnLocation == null) {
            server.scheduler.runTaskLater(getInstance(), Runnable {
                randomTeleport(p)
            }, 4)
        }
    }



    @EventHandler
    fun onPaperServerListPing(e: PaperServerListPingEvent) {
        e.motd(text("ADVANCEMENT TRAVELER", NamedTextColor.RED, TextDecoration.BOLD))
        e.playerSample.clear()
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        val p = e.player
        val block = e.clickedBlock?.type

        if (p.uniqueId.toString() !in runner) {
            if (e.action == Action.LEFT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_BLOCK) {
                if (block != null && !block.isAir && block == Material.END_PORTAL_FRAME || block == Material.DRAGON_EGG) {
                    e.isCancelled = true
                    p.sendMessage(text("밸런스를 위해 이 블록에 상호작용이 불가능합니다.", NamedTextColor.RED))
                }
            }
        }
    }

    @EventHandler
    fun onPlayerAttemptItemPickup(e: PlayerAttemptPickupItemEvent) {
        val p = e.player
        val item = e.item.itemStack

        if (item.type == Material.DRAGON_EGG && p.uniqueId.toString() !in runner) {
            e.isCancelled = true
        }
    }
}