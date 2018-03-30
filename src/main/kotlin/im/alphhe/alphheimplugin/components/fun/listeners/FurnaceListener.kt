/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.`fun`.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.inventory.ItemStack

class ChickenFurnaceListener : Listener {

    val chickenName = ChatColor.translateAlternateColorCodes('&', "&c[&6Cat Meat&c]")
    val cookedName = ChatColor.translateAlternateColorCodes('&', "&c[&6Cooked \"Chicken\"&c]")

    @EventHandler
    fun eventHandler(e: FurnaceSmeltEvent) {
        if (e.source.type == Material.RAW_CHICKEN && e.source?.itemMeta?.displayName == chickenName) {
            val itemStack = ItemStack(Material.COOKED_CHICKEN)
            val itemMeta = itemStack.itemMeta
            itemMeta.displayName = cookedName
            itemStack.itemMeta = itemMeta
            e.result = itemStack

        }

    }
}
