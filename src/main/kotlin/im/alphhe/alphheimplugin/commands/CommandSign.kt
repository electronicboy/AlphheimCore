/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CatchUnknown
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.entity.Player

class CommandSign(plugin: AlphheimCore) : AlphheimCommand(plugin, "editsign") {

    @CatchUnknown
    @CommandPermission("alphheim.mod")
    fun editSign(sender: Player, line: Int, text: String) {
        val block = sender.getTargetBlock(mutableSetOf(Material.AIR), 8)

        if (block.type != Material.WALL_SIGN && block.type != Material.SIGN_POST) {
            MessageUtil.sendError(sender, "You must be facing a sign!")
            return
        }

        val sign = block.state as Sign

        if (0 < line && line <= sign.lines.size) {
            val newText = ChatColor.translateAlternateColorCodes('&', text)
            sign.setLine(line -1, newText)
            sign.update()

        } else {
            MessageUtil.sendError(sender, "Invalid row number!")
        }



    }


}