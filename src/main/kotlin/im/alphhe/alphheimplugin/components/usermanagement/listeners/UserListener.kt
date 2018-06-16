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

    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        userManager.plugin.server.scheduler.runTaskLater(userManager.plugin, {
            if (!e.player.isOnline) return@runTaskLater // They logged off...
            userManager.plugin.server.onlinePlayers.forEach {
                if (it.canSee(e.player)) {
                    it.sendActionBar(MessageUtil.format(e.player.name, "+", ChatColor.GREEN))
                }
            }
        }, 10L)
    }

    @EventHandler
    fun playerJoin(e: PlayerQuitEvent) {
        userManager.plugin.server.onlinePlayers.forEach {
            if (it.canSee(e.player)) {
                it.sendActionBar(MessageUtil.format(e.player.name, "-", ChatColor.RED))
            }
        }

    }
}
