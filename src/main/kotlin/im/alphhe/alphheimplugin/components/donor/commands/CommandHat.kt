/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.commands

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.Material
import org.bukkit.entity.Player

class CommandHat(private var plugin: AlphheimCore) : AlphheimCommand(plugin, "hat") {

    @Default
    @CommandPermission("alphheim.hat")
    fun onHat(player: Player, @Default("wear") action: String) {
        // TODO: Handle remove?
        if (player.itemInHand?.type != Material.AIR) {
            val inventory = player.inventory

            // Get items....
            val handItem = inventory.itemInHand
            val hatItem = inventory.helmet


            // hand to helmet, helmet to hand; This should technically be safe.
            inventory.helmet = handItem
            inventory.itemInHand = hatItem

            MessageUtil.sendInfo(player, "Lookin' pretty good!")


        }


    }

}