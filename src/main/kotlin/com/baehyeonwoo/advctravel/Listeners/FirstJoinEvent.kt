package com.baehyeonwoo.advctravel.Listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
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
        val random = Random()

        var x = random.nextInt(500)
        val y = 120
        var z = random.nextInt(500)

        if(x < 300) {
            x += random.nextInt(150)
        }
        if(z < 300) {
            z += random.nextInt(150)
        }

        if(random.nextInt(2) == 1) {
            x = -x
        }
        if(random.nextInt(2) == 1) {
            z = -z
        }
        val loc = e.player.location
        loc.x = x.toDouble()
        loc.y = y.toDouble()
        loc.z = z.toDouble()
        e.player.teleport(loc)
    }
}