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

    private val players = AdvcTpaKommand.players

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val to = e.to
        val from = e.from

        if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[0]} ) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                val sender = p
                val receiver = Bukkit.getPlayer(UUID.fromString(players.keys.first { x -> sender.uniqueId.toString() == x.split('/')[0] }.split('/')[1]))

                sender.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다. ",NamedTextColor.RED))
                receiver?.sendMessage(text("상대방의 움직임이 감지되어 텔레포트가 취소되었습니다. ",NamedTextColor.RED))

                AdvcTpaKommand.tpaMap.remove(sender.uniqueId)
                players["${sender.uniqueId}/${receiver?.uniqueId}"]?.cancel()
                players.remove("${sender.uniqueId}/${receiver?.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
        else if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[1]}) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                val receiver = p
                val sender = Bukkit.getPlayer(UUID.fromString(players.keys.first { x -> receiver.uniqueId.toString() == x.split('/')[1] }.split('/')[0]))

                sender?.sendMessage(text("상대방의 움직임이 감지되어 텔레포트가 취소되었습니다. ",NamedTextColor.RED))
                receiver.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다. ",NamedTextColor.RED))

                AdvcTpaKommand.tpaMap.remove(sender?.uniqueId)
                players["${sender?.uniqueId}/${receiver.uniqueId}"]?.cancel()
                players.remove("${sender?.uniqueId}/${receiver.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
    }
}