/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignListener(plugin: AlphheimCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onSignUpdate(e: SignChangeEvent) {
        if (!e.player.hasPermission("alphheim.mod")) return
        for (line in 0..3) {
            e.setLine(line, ChatColor.translateAlternateColorCodes('&', e.getLine(line)))
        }
    }

}