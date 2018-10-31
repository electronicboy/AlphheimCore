/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.restart.listener

import im.alphhe.alphheimplugin.components.restart.RestartHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit

class RestartListener(private val handler: RestartHandler) : Listener {

    private var shutdownRunnanble: BukkitRunnable? = null;

    init {
        handler.plugin.server.pluginManager.registerEvents(this, handler.plugin);
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun playerLogout(e: PlayerQuitEvent) {
        if (handler.getStatus()) {
            shutdownRunnanble = object : BukkitRunnable() {

                override fun run() {
                    if (handler.getStatus() && handler.plugin.server.onlinePlayers.isEmpty()) {
                        handler.plugin.server.shutdown()
                    }
                }
            }

            shutdownRunnanble!!.runTaskLater(handler.plugin, TimeUnit.MINUTES.toSeconds(10) * 20)

        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun playerLogin(e: PlayerJoinEvent) {
        val runnable = shutdownRunnanble;
        if (runnable != null) {
            runnable.cancel()
            shutdownRunnanble = null
        }
    }

}
