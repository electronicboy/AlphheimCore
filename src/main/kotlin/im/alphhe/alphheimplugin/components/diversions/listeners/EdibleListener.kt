/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.diversions.listeners

import im.alphhe.alphheimplugin.EladriaCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class EdibleListener(val plugin: EladriaCore) : Listener {

    private val regex = "([sS])hane(')?(s)? ([Cc])ooking".toRegex()

    @EventHandler
    fun onConsume(e: PlayerItemConsumeEvent) {
        Runnable {
            val item = e.item
            if (item.itemMeta?.displayName?.contains(regex) == true) { // Don't think about it
                plugin.server.scheduler.runTask(plugin, Runnable {
                    e.player.kickPlayer("You have died a dreadful death...")
                })
            }
        }
    }
}