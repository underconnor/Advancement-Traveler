package com.baehyeonwoo.advctravel.plugin.objects

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.annotations.Nullable

class AdvcTpaObject(var sender: Player, var receiver: Player, var isAccepted: Boolean, var expiredTask: BukkitTask, @Nullable var waitTask: BukkitTask?)