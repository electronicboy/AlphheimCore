/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.rewards

import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemVoteReward(voteHandler: VoteHandler, private val itemStack: ItemStack, private var itemDescription: String?) : IVoteReward {

    constructor(voteHandler: VoteHandler, itemStack: ItemStack) : this(voteHandler, itemStack, null)

    init {
        if (itemDescription == null) {
            val nmsCopy = CraftItemStack.asNMSCopy(itemStack)
            itemDescription = nmsCopy.item.a(nmsCopy)
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
