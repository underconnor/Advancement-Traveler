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
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

/***
 * @author PyBsh
 */

class AdvcTpaListener: Listener {

    val tpaMap = AdvcTpaKommand.tpaMap

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val p = e.player
        val to = e.to
        val from = e.from

        if (tpaMap.values.any { x -> x.sender == p && x.accepted} ) {
            if (from.x != to.x || from.y != to.y || from.z != to.z) {
                val sender = p // 안햇갈릴려고 한거임 건들지마셈
                val receiver = tpaMap.values.first { x -> x.sender == p }.receiver

                sender.sendMessage(text("움직임이 감지되어 ${receiver.name}님으로의 텔레포트 요청이 취소되었습니다. ", NamedTextColor.RED))
                receiver.sendMessage(text("상대방의 움직임이 감지되어 ${sender.name}님의 텔레포트 요청이 취소되었습니다. ", NamedTextColor.RED))

                tpaMap[sender]?.waitTask?.cancel()
                tpaMap.remove(sender)
            }
        }
        else if (tpaMap.values.any { x -> x.receiver == p && x.accepted}) {
            if (from.x != to.x || from.y != to.y || from.z != to.z) {
                val receiver = p // 안햇갈릴려고 한거임 건들지마셈
                val sender = tpaMap.values.first { x -> x.receiver == p }.sender

                sender.sendMessage(text("상대방의 움직임이 감지되어 ${receiver.name}님으로의 텔레포트가 취소되었습니다. ", NamedTextColor.RED))
                receiver.sendMessage(text("움직임이 감지되어 ${sender.name}님의 텔레포트 요청이 취소되었습니다. ", NamedTextColor.RED))

                tpaMap[sender]?.waitTask?.cancel()
                tpaMap.remove(sender)
            }
        }
    }

    @EventHandler
    fun OnPlayerQuit(e: PlayerQuitEvent){
        val sender = e.player

        if(tpaMap.keys.any { x -> x == sender}){
            tpaMap[sender]?.expirTask?.cancel()
            tpaMap[sender]?.waitTask?.cancel()

            tpaMap[sender]?.receiver?.sendMessage(text("얘! 텔레포트 요청을 보낸 ${sender.name} 가 도망갔어! (요청 취소)", NamedTextColor.RED))
            tpaMap.remove(sender)
        }
    }
}