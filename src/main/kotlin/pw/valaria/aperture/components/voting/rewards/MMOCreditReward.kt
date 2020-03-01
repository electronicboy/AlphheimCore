/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting.rewards

import org.bukkit.entity.Player
import pw.valaria.aperture.components.mmocredits.MMOCreditsHandler
import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.utils.MessageUtil

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
