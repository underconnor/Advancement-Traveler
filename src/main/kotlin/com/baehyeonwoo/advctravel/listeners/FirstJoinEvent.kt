/*
 * Copyright (c) 2021 BaeHyeonWoo & Others
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

package com.baehyeonwoo.advctravel.listeners

import com.baehyeonwoo.advctravel.AdvcTravelMain
import com.baehyeonwoo.advctravel.utils.randomTeleport
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/***
 * Original author FSanchir, Reconstructed by PatrcikKR.
 */

class FirstJoinEvent : Listener {
    @EventHandler
    fun onFirstJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPlayedBefore()) {
            AdvcTravelMain.instance.server.scheduler.runTaskLater(AdvcTravelMain.instance, Runnable {
                randomTeleport(event.player)
            }, 4)
        }
    }
}