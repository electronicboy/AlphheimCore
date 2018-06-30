/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.rewards

import im.alphhe.alphheimplugin.components.mmocredits.MMOCreditsHandler
import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player

class MMOCreditReward(private val voteHandler: VoteHandler, private val amount: Int) : IVoteReward {
    override fun process(player: Player): Boolean {
        val creditHandler = voteHandler.plugin.componentHandler.getComponent(MMOCreditsHandler::class.java)
        if (creditHandler == null ) {
            voteHandler.plugin.logger.warning("Missing MMO component handler!")
        } else if (creditHandler.giveCredits(player, amount)) {
            MessageUtil.sendInfo(player, "You have received $amount mmo credits! Use /credits redeem to redeem them!")
        }

        return true
    }

}
