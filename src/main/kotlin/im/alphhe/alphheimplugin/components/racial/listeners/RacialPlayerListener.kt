/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial.listeners

import im.alphhe.alphheimplugin.components.racial.RacialHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RacialPlayerListener(private val racialHandler: RacialHandler) : Listener {

    init {
        racialHandler.plugin.server.pluginManager.registerEvents(this, racialHandler.plugin)
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        racialHandler.applyEffects(e.player)
    }
}