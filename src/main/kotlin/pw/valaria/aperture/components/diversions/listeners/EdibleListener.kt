/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.diversions.listeners

import pw.valaria.aperture.ApertureCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class EdibleListener(val plugin: ApertureCore) : Listener {

    private val regex = "([sS])hane(')?(s)? ([Cc])ooking".toRegex()

    @EventHandler
    fun onConsume(e: PlayerItemConsumeEvent) {
        Runnable {
            val item = e.item
            if (item.itemMeta?.displayName?.contains(regex) == true) {
                plugin.server.scheduler.runTask(plugin, Runnable {
                    e.player.kickPlayer("You have died a dreadful death...")
                })
            }
        }
    }
}