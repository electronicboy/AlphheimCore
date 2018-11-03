/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.listeners

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.health.HealthHandler
import im.alphhe.alphheimplugin.components.tablist.TabListHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerListener(private val plugin: EladriaCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onAsyncJoin(e: AsyncPlayerPreLoginEvent) {
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(e.uniqueId)
        user.updateData() // ensure that user data is updated from the db
        user.setLastNick(e.name)

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val tabListHandler = plugin.componentHandler.getComponent(TabListHandler::class.java)
        if (tabListHandler != null) {
            plugin.server.scheduler.runTaskLater(plugin, { tabListHandler.setSB(e.player) }, 10L)
        }

        plugin.componentHandler.getComponent(HealthHandler::class.java)?.updateHealth(e.player)

        object : BukkitRunnable() {
            override fun run() {
                if (e.player.isOnline)
                    plugin.componentHandler.getComponent(VoteHandler::class.java)?.processPlayerLogin(e.player)

                if (plugin.server.hasWhitelist() && e.player.hasPermission("alphheim.mod")) {
                    MessageUtil.sendError(e.player, "Server is whitelisted!")
                }
            }

        }.runTaskLater(plugin, 10)


    }
}