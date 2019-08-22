/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.listeners

import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.health.HealthHandler
import pw.valaria.aperture.components.tablist.TabListHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.utils.MessageUtil
import net.md_5.bungee.api.ChatColor
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerListener(private val plugin: ApertureCore) : Listener {

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
        e.joinMessage = null
        val tabListHandler = plugin.componentHandler.getComponent(TabListHandler::class.java)
        if (tabListHandler != null) {
            plugin.server.scheduler.runTaskLater(plugin, Runnable { tabListHandler.setSB(e.player) }, 10L)
        }

        plugin.componentHandler.getComponent(HealthHandler::class.java)?.updateHealth(e.player)

        object : BukkitRunnable() {
            override fun run() {
                if (e.player.isOnline)
                    plugin.componentHandler.getComponent(VoteHandler::class.java)?.processPlayerLogin(e.player)

                if (plugin.server.hasWhitelist() && e.player.hasPermission("group.mod")) {
                    MessageUtil.sendError(e.player, "Server is whitelisted!")
                }
            }

        }.runTaskLater(plugin, 10)


    }

    @EventHandler
    fun kickBeautifier(e: PlayerKickEvent) {
        val reason = e.reason


        e.reason = "${ChatColor.DARK_GRAY}[${ChatColor.RED}${plugin.getServerName()}${ChatColor.DARK_GRAY}]" +
                "\n\n" +
                "${ChatColor.RED}You have been kicked from the server" +

                if (reason != "") {
                    ":\n $reason"
                } else ""

    }

    @EventHandler
    fun phantomsNoCreative(e: PhantomPreSpawnEvent) {
        if (((e.spawningEntity as? Player)?.gameMode) == GameMode.CREATIVE) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun cleanCommands(e: PlayerCommandSendEvent) {
        if (e.player.hasPermission("group.admin")) return // groups, yay

        e.commands.removeIf { command -> command.contains(':', false)}
    }
}