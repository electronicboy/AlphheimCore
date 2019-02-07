/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.restart.command

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.AlphheimCommand
import pw.valaria.aperture.components.restart.RestartHandler
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.command.CommandSender

@CommandAlias("restart")
class CommandRestart(private val handler: RestartHandler, plugin: ApertureCore) : AlphheimCommand(plugin) {

    @CommandPermission("alphheim.admin")
    @Subcommand("get|status")
    @Default
    fun getStatus(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
    }

    @CommandPermission("alphheim.admin")
    @Subcommand("set")
    fun setStatus(sender: CommandSender, newStatus: Boolean ) {
        handler.setStatus(newStatus)
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
        MessageUtil.broadcast("alphheim.admin", "pending restart status: ${handler.getStatus()}")
    }

    @CommandPermission("alphheim.admin")
    @Subcommand("now")
    fun restartNow(@Optional reason: String?) {
        if (reason == null) {
            handler.plugin.restart()
        } else {
            handler.plugin.restart(reason)
        }
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }

}
