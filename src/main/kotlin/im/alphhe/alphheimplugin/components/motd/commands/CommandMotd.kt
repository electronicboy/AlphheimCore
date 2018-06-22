/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.motd.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.motd.MotdHandler
import org.bukkit.command.CommandSender

@CommandAlias("motd")
class CommandMotd(val handler: MotdHandler) : AlphheimCommand(handler.plugin) {

    @Default
    fun onCommand(sender: CommandSender): Unit {
        handler.listener.sendMotd(sender)
    }


}