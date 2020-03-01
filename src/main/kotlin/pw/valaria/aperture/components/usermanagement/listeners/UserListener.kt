/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.usermanagement.listeners

import net.md_5.bungee.api.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MessageUtil

class UserListener(private val userManager: UserManager) : Listener {

    init {
        userManager.plugin.server.pluginManager.registerEvents(this, userManager.plugin)
    }


    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        e.joinMessage = null
        userManager.plugin.server.scheduler.runTaskLater(userManager.plugin, Runnable {
            if (e.player.isOnline) {
                userManager.plugin.server.onlinePlayers.forEach {
                    if (it.canSee(e.player)) {
                        it.sendActionBar(MessageUtil.format("+", ChatColor.GREEN) + "${ChatColor.GREEN} ${e.player.name}")
                    }
                }
            }
        }, 10L)
    }

    @EventHandler
    fun playerJoin(e: PlayerQuitEvent) {
        e.quitMessage = null
        userManager.plugin.server.onlinePlayers.forEach {
            if (it.canSee(e.player)) {
                it.sendActionBar(MessageUtil.format("-", ChatColor.RED) + "${ChatColor.GREEN} ${e.player.name}")
            }
        }

    }
}
