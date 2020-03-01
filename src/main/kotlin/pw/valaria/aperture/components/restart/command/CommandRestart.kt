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
import org.bukkit.command.CommandSender
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.restart.RestartHandler
import pw.valaria.aperture.utils.MessageUtil

@CommandAlias("restart")
class CommandRestart(private val handler: RestartHandler, plugin: ApertureCore) : CoreCommand(plugin) {

    @CommandPermission("group.admin")
    @Subcommand("get|status")
    @Default
    fun getStatus(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
    }

    @CommandPermission("group.admin")
    @Subcommand("set")
    fun setStatus(sender: CommandSender, newStatus: Boolean ) {
        handler.setStatus(newStatus)
        MessageUtil.sendInfo(sender, "pending restart status: ${handler.getStatus()}" )
        MessageUtil.broadcast("group.admin", "pending restart status: ${handler.getStatus()}")
    }

    @CommandPermission("group.admin")
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
