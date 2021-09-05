package com.baehyeonwoo.advctravel.Listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
import com.baehyeonwoo.advctravel.utils.RandomTeleporter
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class RespawnEvent : Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        val bukkitrunnable = object: BukkitRunnable() {
            override fun run() {
                RandomTeleporter().RandomTeleport(e.player)
            }
        }
        bukkitrunnable.runTaskLater(getInstance(), 4)

    }
}