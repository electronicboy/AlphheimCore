/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player

@CommandAlias("fix|repair")
class CommandFix(private val plugin: AlphheimCore) : AlphheimCommand(plugin) {

    @Subcommand("hand")
    @CommandAlias("fixhand")
    @CommandPermission("alphheim.fix")
    fun onHand(sender: Player) {
        val item = sender.itemInHand
        if (item == null || item.type.isBlock || item.durability == 0.toShort() || item.type.maxDurability < 1) {
            MessageUtil.sendError(sender, "You cannot repair this item!")
            return
        }
        if (checkCooldown(sender, "fixCooldown")) {
            item.durability = 0
            sender.itemInHand = item // This item is already there, but this solves issues with updates
        }

    }


    @CommandAlias("fixall")
    @CommandPermission("alphheim.fixall")
    @Subcommand("all")
    fun onAll(sender: Player) {
        if (!checkCooldown(sender, "fixCooldown")) return

        // This command currently isn't given out to the public, so...
        sender.inventory.contents.forEach {
            if (it != null && !it.type.isBlock && it.durability != 0.toShort() && it.type.maxDurability > 0.toShort()) {
                it.durability = 0
            }
        }

        sender.inventory.armorContents.forEach {
            if (it != null && !it.type.isBlock && it.durability != 0.toShort() && it.type.maxDurability > 0.toShort()) {
                it.durability = 0
            }
        }

        sender.updateInventory()

        MessageUtil.sendInfo(sender, "Items repaired!")

    }


}
