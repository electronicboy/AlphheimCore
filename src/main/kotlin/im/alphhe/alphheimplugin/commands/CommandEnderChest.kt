/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("enderchest|ec")
class CommandEnderChest(plugin: AlphheimCore) : AlphheimCommand(plugin) {

    @CommandPermission("alphheim.enderchest")
    @Default
    fun enderChest(sender: Player, @Flags("defaultself") target: Player) {
        sender.openInventory(target.player.enderChest)
    }

    @HelpCommand
    fun unknownCommand(sender: CommandSender, help: CommandHelp) {
        help.showHelp()
    }
}
