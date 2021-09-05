package com.baehyeonwoo.advctravel.Listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
import com.baehyeonwoo.advctravel.utils.RandomTeleporter
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FirstJoinEvent : Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }
    @EventHandler
    fun onFirstJoin(e: PlayerJoinEvent) {
        if(e.player.hasPlayedBefore()) {
            return
        }
        val bukkitrunnable = object: BukkitRunnable() {
            override fun run() {
                RandomTeleporter().RandomTeleport(e.player)
            }
        }
        bukkitrunnable.runTaskLater(getInstance(), 4)
    }
}