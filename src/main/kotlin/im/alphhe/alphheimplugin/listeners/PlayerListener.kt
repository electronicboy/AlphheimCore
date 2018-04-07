/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListener(private val plugin: AlphheimCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onAsyncJoin(e: AsyncPlayerPreLoginEvent) {
        this.plugin.userManager.getUser(e.uniqueId)

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        plugin.server.scheduler.runTaskLater(plugin, {this.plugin.tabListHandler.setSB(e.player)}, 10L)
        plugin.healthHandler.updateHealth(e.player)

    }
}