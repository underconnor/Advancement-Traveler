package com.baehyeonwoo.advctravel.Listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
import com.baehyeonwoo.advctravel.utils.RandomTeleporter
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import java.util.*

class FirstJoinEvent : Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val config = getInstance().config

    @EventHandler
    fun onFirstJoin(e: PlayerJoinEvent) {
        if(e.player.hasPlayedBefore()) {
            return
        }
        RandomTeleporter().RandomTeleport(e.player)
    }
}