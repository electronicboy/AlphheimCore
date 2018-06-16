/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.restart.command

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.restart.RestartHandler
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.command.CommandSender

class CommandRestart(private val handler: RestartHandler, plugin: AlphheimCore) : AlphheimCommand(plugin, "restart") {

    @CommandPermission("alphheim.dev")
    @Subcommand("get|status")
    @Default
    fun getStatus(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
    }

    @CommandPermission("alphheim.dev")
    @Subcommand("set")
    fun setStatus(sender: CommandSender, newStatus: Boolean ) {
        handler.setStatus(newStatus)
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
    }

}
