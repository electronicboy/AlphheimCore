/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.rewards

import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player

class MMOCreditReward(private val voteHandler: VoteHandler, private val amount: Int) : IVoteReward {
    override fun process(player: Player): Boolean {
        if (voteHandler.plugin.creditsHandler.giveCredits(player, amount)) {
            MessageUtil.sendInfo(player, "You have received $amount mmo credits! Use /redeem to redeem them!")
        }

        return true
    }

}
