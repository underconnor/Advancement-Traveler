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

    private val players = AdvcTpaKommand.players

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val to = e.to
        val from = e.from

        if(players.keys.any { x -> x.sender == p }) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다.",NamedTextColor.RED))

                val sender = p
                val receiver = players.keys.filter { x -> x.sender == sender }[0].receiver

                players[AdvcTpaReady(sender, receiver)]?.cancel()
                players.remove(AdvcTpaReady(sender, receiver))
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
        else if(players.keys.any { x -> x.receiver == p }) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다.",NamedTextColor.RED))

                val receiver = p
                val sender = players.keys.filter { x -> x.receiver == receiver }[0].sender

                players[AdvcTpaReady(sender, receiver)]?.cancel()
                players.remove(AdvcTpaReady(sender, receiver))
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
    }
}