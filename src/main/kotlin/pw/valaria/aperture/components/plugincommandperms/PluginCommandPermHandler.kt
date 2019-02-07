/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.plugincommandperms

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginEnableEvent

private val NO_PERM_MESSAGE = "${ChatColor.DARK_GRAY}[${ChatColor.RED}A${ChatColor.DARK_GRAY}] ${ChatColor.DARK_RED}You do not have permission to use this command!"

class PluginCommandPermHandler(plugin: ApertureCore) : AbstractHandler(plugin), Listener {


    init {
        plugin.server.pluginManager.registerEvents(this, plugin)

        plugin.server.scheduler.scheduleSyncDelayedTask(plugin) {
            handleCommands()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun pluginEnable(e: PluginEnableEvent) {
        handleCommands()
    }


    private fun handleCommands() {
        val simpleCommandMap = Bukkit.getCommandMap() as SimpleCommandMap

        simpleCommandMap.commands.forEach({
            if (it.permissionMessage == null) {
                it.permissionMessage = NO_PERM_MESSAGE
            }

        })

    }

}

