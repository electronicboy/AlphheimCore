/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.nicks.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.entity.Player
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

        if (e.player.hasPermission("alphheim.mod")) {
            plugin.server.scheduler.runTaskLater(plugin, {showCount(e.player)}, 10L)
        }

    }

    private fun showCount(player: Player) {
        MessageUtil.sendInfo(player, "nick request count pending!")
    }
}