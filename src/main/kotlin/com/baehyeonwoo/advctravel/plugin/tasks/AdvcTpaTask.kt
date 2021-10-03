package com.baehyeonwoo.advctravel.plugin.tasks

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import com.baehyeonwoo.advctravel.plugin.commands.AdvcTpaKommand
import com.baehyeonwoo.advctravel.plugin.commands.AdvcTpaKommand.receiveTpaDelay
import com.baehyeonwoo.advctravel.plugin.commands.AdvcTpaKommand.sendTpaDelay
import com.baehyeonwoo.advctravel.plugin.objects.AdvcTpaObject
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object AdvcTpaTask {

    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val scheduler = server.scheduler

    /*fun countdown(sender: Player) {
        val receiver = AdvcTpaKommand.tpaMap[sender]?.receiver
        val startTime = System.currentTimeMillis()

        val scheudle = scheduler.scheduleSyncRepeatingTask(
            getInstance(), {
                if (AdvcTpaKommand.tpaMap[sender]?.isAccepted != true) {
                    sender.sendActionBar(text("텔레포트가 취소되었습니다", NamedTextColor.RED))
                    receiver?.sendActionBar(text("텔레포트가 취소되었습니다", NamedTextColor.RED))

                } else {
                    sender.sendActionBar(
                        text(
                            "${29 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime)}초 후 텔레포트합니다...",
                            NamedTextColor.GOLD
                        )
                    )
                    receiver?.sendActionBar(
                        text(
                            "${29 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime)}초 후 텔레포트합니다...",
                            NamedTextColor.GOLD
                        )
                    )
                }
            },
            0L, 0L)
    }*/

    fun acceptTask(sender: Player, receiver: Player) {
        val acceptTask = scheduler.runTaskLater(getInstance(), Runnable {
            sender.uniqueId.sendTpaDelay = System.currentTimeMillis()
            receiver.uniqueId.receiveTpaDelay = System.currentTimeMillis()
            sender.sendMessage(text("텔레포트중입니다...", NamedTextColor.GOLD))
            sender.teleport(receiver.location)
            AdvcTpaKommand.tpaMap.remove(sender)
        }, 600L)

        AdvcTpaKommand.tpaMap[sender]?.waitTask = acceptTask
    }

    fun waitTask(sender: Player, receiver: Player) {
        val waitTask = scheduler.runTaskLater(
            getInstance(),
            Runnable {
                AdvcTpaKommand.tpaMap.remove(sender)
                sender.sendMessage(text("얘! 요청이 만료되었단다!", NamedTextColor.GOLD))
                receiver.sendMessage(text("얘! ${sender.name} 에게 온 요청이 만료되었단다!", NamedTextColor.GOLD))
            },
            2400L
        )

        AdvcTpaKommand.tpaMap[sender] = AdvcTpaObject(sender, receiver, false, waitTask, null)
    }
}