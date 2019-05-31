/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import com.destroystokyo.paper.MaterialTags
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player

@CommandAlias("editsign")
class CommandSign(plugin: ApertureCore) : CoreCommand(plugin) {

    @CatchUnknown
    @CommandPermission("alphheim.mod")
    fun editSign(sender: Player, line: Int, text: String) {
        val sign = getSign(sender)

        if (sign == null) {
            MessageUtil.sendError(sender, "You must be facing a sign!")
            return
        }

        if (0 < line && line <= sign.lines.size) {
            val newText = ChatColor.translateAlternateColorCodes('&', text)
            sign.setLine(line - 1, newText)
            sign.update()

        } else {
            MessageUtil.sendError(sender, "Invalid row number!")
        }
    }


    @Subcommand("list")
    @CommandPermission("alphheim.mod")
    fun getLines(sender: Player) {
        val sign = getSign(sender)

        if (sign == null) {
            MessageUtil.sendError(sender, "You must be facing a sign!")
            return
        }

        val lines = sign.lines

        for (lineNo in 0..(lines.size - 1)) {
            MessageUtil.sendInfo(sender, "${lineNo + 1}:${ChatColor.RESET} ${lines[lineNo]}")
        }

    }

    private fun getSign(player: Player) : Sign? {
        val block = player.getTargetBlock(mutableSetOf(Material.AIR), 8)

        if (!MaterialTags.SIGNS.isTagged(block)) {
            return null
        }

        return block.state as Sign?
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}