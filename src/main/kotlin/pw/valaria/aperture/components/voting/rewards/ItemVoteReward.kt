/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting.rewards

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.utils.MessageUtil

class ItemVoteReward(@Suppress("UNUSED_PARAMETER") voteHandler: VoteHandler, private val itemStack: ItemStack, private var itemDescription: String?) : IVoteReward {

    constructor(voteHandler: VoteHandler, itemStack: ItemStack) : this(voteHandler, itemStack, null)

    init {
        if (itemDescription == null) {
            itemDescription = itemStack.i18NDisplayName
        }
    }

    override fun process(player: Player): Boolean {
        val message = "You have received " + "${itemStack.amount}x $itemDescription"
        MessageUtil.sendInfo(player, message)

        val addItem = player.inventory.addItem(itemStack.clone())
        for (itemEntry in addItem.entries) {
            player.location.world.dropItemNaturally(player.location, itemEntry.value)
        }
        return true
    }

}
