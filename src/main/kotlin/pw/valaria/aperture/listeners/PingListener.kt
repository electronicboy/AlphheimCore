/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.listeners

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pw.valaria.aperture.ApertureCore

public class PingListener(val plugin: ApertureCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onPing(e: PaperServerListPingEvent) {
        if (e.protocolVersion == -1) {
            e.version = "Starting..."
        }
    }
}