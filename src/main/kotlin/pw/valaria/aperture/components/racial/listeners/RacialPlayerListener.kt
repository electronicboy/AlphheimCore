/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.racial.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import pw.valaria.aperture.components.racial.RacialHandler

class RacialPlayerListener(private val racialHandler: RacialHandler) : Listener {

    init {
        racialHandler.plugin.server.pluginManager.registerEvents(this, racialHandler.plugin)
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        racialHandler.applyEffects(e.player)
    }
}