/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.motd.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import pw.valaria.aperture.commands.AlphheimCommand
import pw.valaria.aperture.components.motd.MotdHandler
import org.bukkit.command.CommandSender

@CommandAlias("motd")
class CommandMotd(val handler: MotdHandler) : AlphheimCommand(handler.plugin) {

    @Default
    fun onCommand(sender: CommandSender): Unit {
        handler.listener.sendMotd(sender)
    }


}