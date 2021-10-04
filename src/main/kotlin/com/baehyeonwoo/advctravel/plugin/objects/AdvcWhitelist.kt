package com.baehyeonwoo.advctravel.plugin.objects

import com.google.common.collect.ImmutableSortedSet
import java.io.File
import java.nio.charset.Charset

object AdvcWhitelist {
    lateinit var allows: Set<String>

    fun load(whitelistFile: File) {
        if (!whitelistFile.exists()) {
            whitelistFile.createNewFile()
            whitelistFile.writeText("BaeHyeonWoo\naroxu\nkomq\nssapgosuX\nnorhu1130", Charset.forName("UTF-8"))
        }

        val lines = whitelistFile.readLines()
        allows = ImmutableSortedSet.copyOf(String.CASE_INSENSITIVE_ORDER, lines)
    }
}