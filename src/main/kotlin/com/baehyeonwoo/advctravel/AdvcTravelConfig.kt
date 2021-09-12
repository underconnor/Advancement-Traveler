package com.baehyeonwoo.advctravel

import io.github.monun.tap.config.Config
import io.github.monun.tap.config.ConfigSupport
import java.io.File

object AdvcTravelConfig {
    @Config
    var maxplayers = 1

    @Config
    var administrator = arrayListOf(
        "389c4c9b-6342-42fc-beb3-922a7d7a72f9",
        "5082c832-7f7c-4b04-b0c7-2825062b7638",
        "762dea11-9c45-4b18-95fc-a86aab3b39ee",
        "63e8e8a6-4104-4abf-811b-2ed277a02738",
        "ad524e9e-acf5-4977-9c12-938212663361",
        "3013e38a-74a7-41d4-8e68-71ee440c0e20"
    )

    @Config
    var runner = arrayListOf(
        "389c4c9b-6342-42fc-beb3-922a7d7a72f9"
    )

    fun load(configFile: File) {
        ConfigSupport.compute(this, configFile)
    }
}