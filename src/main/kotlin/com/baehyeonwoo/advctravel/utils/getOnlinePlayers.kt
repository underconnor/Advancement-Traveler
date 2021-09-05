package com.baehyeonwoo.advctravel.utils

import com.baehyeonwoo.advctravel.AdvcTravelMain
import org.bukkit.plugin.Plugin

class getOnlinePlayers {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server
    private val administrator = getInstance().config.getString("administrator").toString()
    fun getOnlinePlayers(): Int {
        var numplayers = 0
        for(p in server.onlinePlayers) {
            if(!administrator.contains(p.uniqueId.toString())) {
                numplayers += 1
            }
        }
        return numplayers
    }
}