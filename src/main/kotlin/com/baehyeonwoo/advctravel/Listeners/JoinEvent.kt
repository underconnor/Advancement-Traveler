package com.baehyeonwoo.advctravel.Listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
import com.baehyeonwoo.advctravel.utils.getOnlinePlayers
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.plugin.Plugin

class JoinEvent: Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val config = getInstance().config
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if(getOnlinePlayers().getOnlinePlayers() > config.getInt("max-players")) {
            e.player.kick(Component.text("서버가 꽉 찼습니다."))
        }
    }
}