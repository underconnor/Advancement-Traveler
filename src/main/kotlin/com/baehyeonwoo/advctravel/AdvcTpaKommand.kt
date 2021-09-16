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

/*
 * Copyright (c) 2021 BaeHyeonWoo
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

import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.HashMap

/***
 * @author PyBsh
 *
 * @co_author BaeHyeonWoo
 */

object AdvcTpaKommand {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    val players: HashMap<Player, BukkitTask> = hashMapOf()

    private val server = getInstance().server

    private val scheduler = server.scheduler

    private val tpaMap: HashMap<UUID, UUID> = HashMap()

    private var UUID.tpaDelay: Long
        get() {
            return timestamps[this] ?: 0
        }
        set(value) {
            timestamps[this] = value
        }

    private val timestamps = HashMap<UUID, Long>()


    private fun tpa(player: Player, target: Player) {

        target.sendMessage(text("10초간 움직이지 마세요, 텔레포트중입니다...", NamedTextColor.GOLD))
        val task = scheduler.runTaskLater(getInstance(), Runnable {
            target.uniqueId.tpaDelay = System.currentTimeMillis()
            target.teleport(player.location)
            target.sendMessage(text("텔레포트중입니다...", NamedTextColor.GOLD))
            players.remove(target)
        }, 200L)

        players[target] = task
    }

    fun advcTpaKommand() {
        getInstance().kommand {
            register("tpa") {
                requires { playerOrNull != null }
                then("target" to player())  {
                    executes {
                        val target: Player by it

                        // 얘! 배먹어라 배! 가을 배가 맛있단다!
                        if (player == target) {
                            player.sendMessage(text("얘! 너 자신에게는 텔레포트를 할 수 없단다!", NamedTextColor.RED))
                        }
                        else if (tpaMap.containsKey(player.uniqueId)) {
                            player.sendMessage(text("얘! 이미 다른사람에게 텔래포트 요청을 했단다!", NamedTextColor.RED))
                        }
                        else if (System.currentTimeMillis() - player.uniqueId.tpaDelay < 600000) {
                            player.sendMessage(text("애! 지금 이 명령어는 쿨타임에 있단다!", NamedTextColor.RED))
                        }
                        else {
                            target.sendMessage(
                                text(
                                    "${player.name}님으로부터 텔레포트 요청이 들어왔습니다.\n" +
                                            "/tpaccept로 요청을 수락 할 수 있습니다.\n" +
                                            "/tpadeny로 요청을 거절 할 수 있습니다.\n" +
                                            "이 요청은 120초 후에 자동으로 만료됩니다.", NamedTextColor.GOLD
                                )
                            )

                            player.sendMessage(
                                text(
                                    "${target.name}님에게 텔레포트 요청을 하였습니다.\n" +
                                            "/tpacancel 명령으로 요청을 취소 할 수 있습니다.\n" +
                                            "이 요청은 120초 후에 자동으로 만료됩니다.", NamedTextColor.GOLD
                                )
                            )
                            tpaMap[player.uniqueId] = target.uniqueId

                            scheduler.runTaskLater(getInstance(), Runnable {
                                val x = tpaMap[player.uniqueId]
                                if (x != null) {
                                    tpaMap.remove(player.uniqueId)
                                    player.sendMessage(text("요청이 만료되었습니다.", NamedTextColor.GOLD))
                                }
                            }, 2400L)
                        }
                    }
                }
            }

            register("tpaaccept") {
                requires { playerOrNull != null }
                executes {
                    if(tpaMap.keys.isEmpty()){
                        player.sendMessage(text("받은 요청이 없습니다.",NamedTextColor.RED))
                    }
                    else if (tpaMap.keys.count { x -> x == player.uniqueId } > 1) {
                        player.sendMessage(text("여러개의 텔레포트 요청이 있습니다. /tpaccept <player>로 요청을 수락하세요."))
                    } else {
                        val request = tpaMap.entries.filter { x -> x.value == player.uniqueId }
                        if (request.isEmpty()) player.sendMessage(text("받은 요청이 없습니다.", NamedTextColor.RED))

                        val target = Bukkit.getPlayer(request[0].key)

                        if (target == null) player.sendMessage(text("요청을 보낸 플레이어가 오프라인입니다.", NamedTextColor.RED))
                        else {
                            tpa(player, target)
                        }
                    }
                }
                then("select" to player()) {
                    requires { playerOrNull != null }
                    executes {
                        if(tpaMap.keys.isEmpty()){
                            player.sendMessage(text("받은 요청이 없습니다.",NamedTextColor.RED))
                        }
                        else {
                            val select: Player by it

                            val request = tpaMap.entries.filter { x -> x.value == player.uniqueId && x.key == select.uniqueId }
                            if (request.isEmpty()) player.sendMessage(text("${select.name}님으로부터 받은 요청이 없습니다.", NamedTextColor.RED))

                            val target = Bukkit.getPlayer(request[0].key)
                            if (target == null) player.sendMessage(text("요청을 보낸 플레이어가 오프라인입니다.", NamedTextColor.RED))
                            else {
                                tpa(player, target)
                            }
                        }
                    }
                }
            }
            register("tpadeny") {
                requires { playerOrNull != null }
                executes {
                    if(tpaMap.keys.isEmpty()){
                        player.sendMessage(text("받은 요청이 없습니다.",NamedTextColor.RED))
                    }
                    else if (tpaMap.keys.count { x -> x == player.uniqueId } > 1) {
                        player.sendMessage(text("여러개의 텔레포트 요청이 있습니다. /tpadeny <player>로 요청을 거절하세요."))
                    } else {
                        val request = tpaMap.entries.filter { x -> x.value == player.uniqueId }
                        if (request.isEmpty()) player.sendMessage(text("받은 요청이 없습니다.", NamedTextColor.RED))

                        val target = Bukkit.getPlayer(request[0].key)

                        if (target == null) player.sendMessage(text("요청을 보낸 플레이어가 오프라인입니다.", NamedTextColor.RED))
                        else {
                            tpaMap.remove(request[0].key)
                            player.sendMessage(text("${target.name}님이 보낸 요청을 거절하였습니다.", NamedTextColor.GOLD))
                            target.sendMessage(text("보낸 요청이 거절되었습니다.", NamedTextColor.RED))
                        }
                    }
                }
                then("select" to player()) {
                    requires { playerOrNull != null }
                    executes {
                        if(tpaMap.keys.isEmpty()) player.sendMessage(text("받은 요청이 없습니다.",NamedTextColor.RED))
                        else {
                            val select: Player by it
                            val request = tpaMap.entries.filter { x -> x.value == player.uniqueId && x.key == select.uniqueId }
                            if (request.isEmpty()) player.sendMessage(text("${select.name}님으로부터 받은 요청이 없습니다.", NamedTextColor.RED))

                            val target = Bukkit.getPlayer(request[0].key)

                            if (target == null) player.sendMessage(text("요청을 보낸 플레이어가 오프라인입니다.", NamedTextColor.RED))
                            else {
                                tpaMap.remove(request[0].key)
                                player.sendMessage(text("${target.name}님이 보낸 요청을 거절하였습니다.", NamedTextColor.GOLD))
                                target.sendMessage(text("보낸 요청이 거절되었습니다.", NamedTextColor.RED))
                            }
                        }
                    }
                }
            }
        }
    }
}