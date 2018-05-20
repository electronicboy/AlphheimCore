/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.GameMode
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
        val user = this.plugin.userManager.getUser(e.uniqueId)
        user.updateData() // ensure that user data is updated from the db
        user.setLastNick(e.name)

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        plugin.server.scheduler.runTaskLater(plugin, { this.plugin.tabListHandler.setSB(e.player) }, 10L)
        plugin.healthHandler.updateHealth(e.player)


    }
}