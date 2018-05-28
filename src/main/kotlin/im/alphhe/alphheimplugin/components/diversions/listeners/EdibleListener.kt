/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.diversions.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class EdibleListener : Listener {

    @EventHandler
    fun onConsume(e: PlayerItemConsumeEvent) {
        val item = e.item
        if (item.itemMeta?.displayName?.contains("([sS])hane(')?(s)? ([Cc])ooking".toRegex()) == true) {
            e.player.kickPlayer("You have died a dreadful death...");
        }
    }
}