/*
 * Copyright (c) 2021 PyBsh
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

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import java.util.*

class AdvcTpaListener: Listener {
    private val players = AdvcTpaKommand.players

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val to = e.to
        val from = e.from

        if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[0]} ) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                val receiver = Bukkit.getPlayer(UUID.fromString(players.keys.first { x ->
                    p.uniqueId.toString() == x.split(
                        '/'
                    )[0]
                }.split('/')[1]))

                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다. ", NamedTextColor.RED))
                receiver?.sendMessage(text("상대방의 움직임이 감지되어 텔레포트가 취소되었습니다. ", NamedTextColor.RED))

                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
                players["${p.uniqueId}/${receiver?.uniqueId}"]?.cancel()
                players.remove("${p.uniqueId}/${receiver?.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
        else if(players.keys.any { x -> p.uniqueId.toString() == x.split('/')[1]}) {
            if (from.x != to.x && from.y != to.y && from.z != to.z) {
                val sender = Bukkit.getPlayer(UUID.fromString(players.keys.first { x ->
                    p.uniqueId.toString() == x.split('/')[1] }.split('/')[0]))

                sender?.sendMessage(text("상대방의 움직임이 감지되어 텔레포트가 취소되었습니다. ", NamedTextColor.RED))
                p.sendMessage(text("움직임이 감지되어 텔레포트가 취소되었습니다. ", NamedTextColor.RED))

                AdvcTpaKommand.tpaMap.remove(sender?.uniqueId)
                players["${sender?.uniqueId}/${p.uniqueId}"]?.cancel()
                players.remove("${sender?.uniqueId}/${p.uniqueId}")
                AdvcTpaKommand.tpaMap.remove(p.uniqueId)
            }
        }
    }
}