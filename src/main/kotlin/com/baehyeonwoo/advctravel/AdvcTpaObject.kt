package com.baehyeonwoo.advctravel

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.annotations.Nullable

class AdvcTpaObject(s: Player, r: Player, acc: Boolean, ext: BukkitTask, @Nullable wat: BukkitTask?) {
    var sender = s
    var receiver = r
    var expirTask = ext
    var accepted = acc
    var waitTask = wat
}