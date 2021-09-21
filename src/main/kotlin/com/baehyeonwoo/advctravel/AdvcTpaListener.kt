package com.baehyeonwoo.advctravel

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import java.util.*

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

        if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[0]} ) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다.",NamedTextColor.RED))

                val sender = p
                val receiver = Bukkit.getPlayer(players.keys.filter { x -> sender.uniqueId.toString() == x.split('/')[0] }[0].split('/')[1])

                players["${sender.uniqueId}/${receiver?.uniqueId}"]?.cancel()
                players.remove("${sender.uniqueId}/${receiver?.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
        else if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[1]}) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다.",NamedTextColor.RED))

                val receiver = p
                val sender = Bukkit.getPlayer(players.keys.filter { x -> receiver.uniqueId.toString() == x.split('/')[1] }[0].split('/')[0])

                players["${sender?.uniqueId}/${receiver.uniqueId}"]?.cancel()
                players.remove("${sender?.uniqueId}/${receiver.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
    }
}