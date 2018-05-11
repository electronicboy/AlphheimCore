/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.rewards

import org.bukkit.entity.Player


interface IVoteReward {

    fun process(player: Player): Boolean

}
