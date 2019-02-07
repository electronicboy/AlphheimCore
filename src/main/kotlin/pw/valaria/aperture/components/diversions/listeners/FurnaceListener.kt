/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.diversions.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.inventory.ItemStack

class FurnaceListener : Listener {

    val chickenName = ChatColor.translateAlternateColorCodes('&', "&c[&6Cat Meat&c]")
    val cookedName = ChatColor.translateAlternateColorCodes('&', "&c[&6Cooked \"Chicken\"&c]")

    @EventHandler
    fun eventHandler(e: FurnaceSmeltEvent) {
        if (e.source.type == Material.RABBIT && e.source?.itemMeta?.displayName == chickenName) {
            val itemStack = ItemStack(Material.COOKED_RABBIT)
            val itemMeta = itemStack.itemMeta
            itemMeta.displayName = cookedName
            itemStack.itemMeta = itemMeta
            e.result = itemStack

        }

    }
}
