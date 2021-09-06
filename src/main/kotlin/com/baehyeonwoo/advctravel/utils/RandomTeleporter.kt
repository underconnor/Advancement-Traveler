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

package com.baehyeonwoo.advctravel.utils

import org.bukkit.entity.Player
import kotlin.random.Random.Default.nextDouble

/***
 * Original author FSanchir, Reconstructed by PatrcikKR.
 */
fun randomTeleport(player: Player) {
    player.teleport(player.location.set(nextCoord(), 120.0, nextCoord()))
}

private tailrec fun nextCoord(): Double {
    val next = nextDouble(-500.0, 500.0)
    return if (next !in -300.0..300.0) next else nextCoord()
}