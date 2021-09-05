package com.baehyeonwoo.advctravel.utils

import org.bukkit.entity.Player
import java.util.*

class RandomTeleporter {
    fun RandomTeleport(p: Player) {
        val random = Random()

        var x = random.nextInt(500)
        val y = 120
        var z = random.nextInt(500)

        if(x < 300) {
            x += random.nextInt(150)
        }
        if(z < 300) {
            z += random.nextInt(150)
        }

        if(random.nextInt(2) == 1) {
            x = -x
        }
        if(random.nextInt(2) == 1) {
            z = -z
        }
        val loc = p.location
        loc.x = x.toDouble()
        loc.y = y.toDouble()
        loc.z = z.toDouble()
        p.teleport(loc)
    }
}