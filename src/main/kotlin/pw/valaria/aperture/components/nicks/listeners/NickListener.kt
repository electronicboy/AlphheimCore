/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.nicks.listeners

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.MySQL
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class NickListener(private val plugin: ApertureCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun join(e: PlayerJoinEvent) {
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(e.player.uniqueId)
        user.setDisplayName(user.getNickname())

        if (e.player.hasPermission("group.mod")) {
            plugin.server.scheduler.runTaskLater(plugin, Runnable { showCount(e.player) }, 10L)
        }

    }

    private fun showCount(player: Player) {
        val runnable = Runnable {
            MySQL.getConnection().use { conn ->
                conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM player_nicks WHERE REQUESTED iS NOT NULL AND STATUS = 0").use {
                    it.executeQuery().use {
                        it.next()
                        val count = it.getInt("COUNT")
                        val countString = if (count == 0) {
                            "no"
                        } else {
                            count.toString()
                        }

                        MessageUtil.sendInfo(player, "There are $countString pending nick requests")


                    }
                }
            }
        }

        if (Bukkit.isPrimaryThread()) {
            MySQL.executor.execute(runnable)
        } else {
            runnable.run()
        }

    }
}