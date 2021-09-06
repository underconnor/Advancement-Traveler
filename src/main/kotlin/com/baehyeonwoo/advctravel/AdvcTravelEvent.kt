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

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.World
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.*
import org.bukkit.event.player.PlayerLoginEvent.Result
import org.bukkit.plugin.Plugin


/***
 * @author BaeHyeonWoo
 */

class AdvcTravelEvent : Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val config = getInstance().config

    private val administrator = requireNotNull(config.getString("administrator"))

    private val runner = requireNotNull(config.getString("runner"))

    private var numPlayers = 0

    private var maxPlayers = 0

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val p = e.player
        p.noDamageTicks = 0
    }

    @EventHandler
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        val advancement = event.advancement

        if (runner.contains(event.player.uniqueId.toString())) {
            if (!advancement.key.toString().startsWith("minecraft:recipes") && !advancement.key.toString().endsWith("root")) {
                maxPlayers = ++server.maxPlayers
                config.set("max-players", maxPlayers)
                getInstance().saveConfig()
            }
        }
    }

    @EventHandler
    fun onAsyncChat(e: AsyncChatEvent) { e.isCancelled = true }

    @EventHandler
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        if (!administrator.contains(e.player.uniqueId.toString())) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block

        if (Tag.BEDS.isTagged(block.type) && player.world.environment != World.Environment.NORMAL) {
            event.isCancelled = true
            player.sendMessage(text("지옥과 엔더에서는 침대가 막혀있습니다!", NamedTextColor.RED))
        }
        if (block.type == Material.RESPAWN_ANCHOR && player.world.environment != World.Environment.NETHER) {
            event.isCancelled = true
            player.sendMessage(text("오버월드와 엔더에서는 리스폰 정박기가 막혀있습니다!", NamedTextColor.RED))
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            var player: Player? = null
            when (val damager = event.damager) {
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
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerLogin(e: PlayerLoginEvent) {
        val p = e.player

        if (administrator.contains(p.uniqueId.toString())) {
            if (e.result == Result.KICK_FULL && !p.isBanned) {
                e.allow()
                server.maxPlayers = ++server.maxPlayers

                numPlayers = server.onlinePlayers.count() - 1
                maxPlayers = --server.maxPlayers
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val p = e.player

        if (administrator.contains(p.uniqueId.toString())) {
            if (!administrator.contains(runner)) {
                numPlayers--
                server.maxPlayers = --server.maxPlayers
            }
        }
        else numPlayers--
    }

    @EventHandler
    fun onPaperServerListPing(event: PaperServerListPingEvent) {
        event.motd(text("ADVANCEMENT TRAVELER", NamedTextColor.RED, TextDecoration.BOLD))
        event.numPlayers = numPlayers
        event.maxPlayers = maxPlayers
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        val p = e.player
        val block = e.clickedBlock?.type

        if (!runner.contains(p.uniqueId.toString())) {
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

        if (item.type == Material.DRAGON_EGG && !runner.contains(p.uniqueId.toString())) {
            e.isCancelled = true
        }
    }
}