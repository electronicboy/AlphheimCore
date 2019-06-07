/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.listeners

import pw.valaria.aperture.ApertureCore
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignListener(plugin: ApertureCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onSignUpdate(e: SignChangeEvent) {
        if (!e.player.hasPermission("group.mod")) return
        for (line in 0..3) {
            e.setLine(line, ChatColor.translateAlternateColorCodes('&', e.getLine(line) ?: ""))
        }
    }

}