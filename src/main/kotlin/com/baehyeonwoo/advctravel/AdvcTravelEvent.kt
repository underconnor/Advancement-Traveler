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
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Tameable
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType
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

    var numPlayers = server.onlinePlayers.size

    var maxPlayers = server.maxPlayers

    private fun randomTeleport(player: Player) {
        val randomX = Random.nextDouble(-1000.0, 1000.0)
        val randomZ = Random.nextDouble(-1000.0, 1000.0)

        player.teleport(player.location.set(randomX, player.world.getHighestBlockYAt(randomX.toInt(), randomZ.toInt()).toDouble(), randomZ))
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val p = e.player
        p.noDamageTicks = 0
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
                server.maxPlayers = ++server.maxPlayers
                maxPlayers = --server.maxPlayers
                config.set("maxplayers", maxPlayers)
                getInstance().saveConfig()
                getInstance().saveConfig()
            }
        }
    }

    @EventHandler
    fun onAsyncChat(e: AsyncChatEvent) {
        val p = e.player

        if (p.uniqueId.toString() !in administrator) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        val p = e.player

        if (p.uniqueId.toString() !in administrator) {
            e.isCancelled = true
        }
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
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.entity is Player) {
            var player: Player? = null
            when (val damager = e.damager) {
                is Player -> {
                    player = damager
                }
                is Tameable -> {
                    if (damager is Wolf) {
                        damager.isAngry = false
                    }

                    player = damager.owner as? Player?
                }
                is Projectile -> {
                    player = damager as? Player?
                }
            }
            if (player != null && !runner.contains(player.uniqueId.toString())) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerLogin(e: PlayerLoginEvent) {
        val p = e.player
        // 1/1
        if (p.uniqueId.toString() in administrator) { // 서버 접속한 사람이 관리자
            if (e.result == Result.KICK_FULL && !p.isBanned) { // 만약에 서버가 꽉찼다면
                if (p.uniqueId.toString() !in runner) { // 그리고 접속하는 사람이 러너가 아니라면
                    e.allow() // 서버접속 허가
                    ++server.maxPlayers // 서버 최대 인원 증가 (만약 1/1 이었다면 2/2)

                    if (numPlayers == 0) {
                        numPlayers = 0
                    }
                    if (numPlayers == 1) {
                        numPlayers = 1
                    } else {
                        server.onlinePlayers.count() - 1
                    }
                    maxPlayers = server.maxPlayers - 1 // 서버목록상으로 1/1
                }
                else {
                    e.allow()
                    ++server.maxPlayers // 만약에 러너라면 서버 최대 인원 증가 (만약 1/1 이었다면 2/2 | 서버상)
                    ++numPlayers // 서버목록 표시상으로 2/2로 만듬
                    maxPlayers = server.maxPlayers
                }
            }
            else ++numPlayers
        }
        else {
            ++numPlayers // 관리자가 아닐시 numPlayers 그냥 증가
        }
        config.set("maxplayers", server.maxPlayers)
        getInstance().saveConfig()
    }


    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val p = e.player

        if (p.uniqueId.toString() in administrator) { // 서버 퇴장한 사람이 관리자
            if (p.uniqueId.toString() !in runner) { // 그리고 나가는 사람이 러너가 아니라면
                numPlayers = server.onlinePlayers.size - 1 // 서버 목록상에서 1/1로 변경
            }
            else { // 나간 사람 러너
                --server.maxPlayers
                --numPlayers
                maxPlayers = server.maxPlayers
            }
        }
        else {
            --numPlayers // 관리자가 아닐시 numPlayers 그냥 감소
        }
        config.set("maxplayers", server.maxPlayers)
        getInstance().saveConfig()
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        server.scheduler.runTaskLater(getInstance(), Runnable {
            randomTeleport(e.player)
        }, 0)
    }



    @EventHandler
    fun onPaperServerListPing(e: PaperServerListPingEvent) {
        e.motd(text("ADVANCEMENT TRAVELER", NamedTextColor.RED, TextDecoration.BOLD))
//        var numP = 0
//        for(i in server.onlinePlayers) {
//            if(config.getString(""))
//        } 잠만요 Tap Config 사용법을 몰라요 ㅋㅋㅋ
        e.numPlayers = numPlayers
        e.maxPlayers = maxPlayers
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
    fun onIM(e: InventoryMoveItemEvent) {
        if (e.source.type == InventoryType.HOPPER) {
            if (e.item.type == Material.DRAGON_EGG) {
                e.isCancelled = true
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