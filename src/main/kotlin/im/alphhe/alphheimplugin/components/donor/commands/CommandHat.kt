/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.Material
import org.bukkit.entity.Player

@CommandAlias("hat")
class CommandHat(private var plugin: EladriaCore) : AlphheimCommand(plugin) {

    @Default
    @CommandPermission("alphheim.hat")
    @Suppress("UNUSED_PARAMETER")
    fun onHat(player: Player /*, @Default("wear") action: String  -- restore me in future*/) {
        // TODO: Handle remove?
        if (player.inventory.itemInMainHand?.type != Material.AIR) {
            val inventory = player.inventory

            // Get items....
            val handItem = inventory.itemInMainHand
            val hatItem = inventory.helmet


            // hand to helmet, helmet to hand; This should technically be safe.
            inventory.helmet = handItem
            inventory.itemInMainHand = hatItem

            MessageUtil.sendInfo(player, "Lookin' pretty good!")


        }


    }

}