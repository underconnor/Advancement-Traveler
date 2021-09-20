package com.baehyeonwoo.advctravel

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class AdvcTpaReady(s: Player, r: Player, t: BukkitTask) {
    val sender = s
    val receiver = r
    val task = t
}