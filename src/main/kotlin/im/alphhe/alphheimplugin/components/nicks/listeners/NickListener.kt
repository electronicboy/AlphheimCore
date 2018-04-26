/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.nicks.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class NickListener(private val plugin: AlphheimCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun join(e: PlayerJoinEvent) {
        val user = plugin.userManager.getUser(e.player.uniqueId)
        e.player.displayName = ChatColor.translateAlternateColorCodes('&', user.getNickname())

    }
}