package com.baehyeonwoo.advctravel

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin

class AdvcTpaListener: Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val scheduler = server.scheduler

    private val players = AdvcTpaKommand.players
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val to = e.to
        val from = e.from

        if(players.contains(p)) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                players.get(p)?.cancel()
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다.",NamedTextColor.RED))
                AdvcTpaKommand.players.remove(p)
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
    }
}