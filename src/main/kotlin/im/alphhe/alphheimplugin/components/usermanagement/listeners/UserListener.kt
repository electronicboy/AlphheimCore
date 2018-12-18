/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.usermanagement.listeners

import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import im.alphhe.alphheimplugin.utils.MessageUtil
import net.md_5.bungee.api.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class UserListener(private val userManager: UserManager) : Listener {

    init {
        userManager.plugin.server.pluginManager.registerEvents(this, userManager.plugin)
    }


    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
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
        userManager.plugin.server.onlinePlayers.forEach {
            if (it.canSee(e.player)) {
                it.sendActionBar(MessageUtil.format("-", ChatColor.RED) + "${ChatColor.GREEN} ${e.player.name}")
            }
        }

    }
}
