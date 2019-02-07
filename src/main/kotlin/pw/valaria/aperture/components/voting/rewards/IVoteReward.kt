/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting.rewards

import org.bukkit.entity.Player


interface IVoteReward {

    fun process(player: Player): Boolean

}
