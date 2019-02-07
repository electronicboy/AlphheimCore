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
import pw.valaria.aperture.ApertureCore
import org.bukkit.entity.Player

@CommandAlias("enderchest|ec")
class CommandEnderChest(plugin: ApertureCore) : CoreCommand(plugin) {

    @CommandPermission("alphheim.enderchest")
    @Default
    fun enderChest(sender: Player, @Flags("defaultself") target: Player) {
        sender.openInventory(target.player.enderChest)
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}
