/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests.inventory

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.Configuration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*


class PlayerChest(val owner: UUID, configuration: Configuration?, val name: String?) : InventoryHolder {
    private val container: Inventory;

    var title: String? = null
    // No config constructor
    constructor(owner: UUID, name: String) : this(owner, null, name)

    init {
        // This is beautiful, but lets try to always have an inventory title
        if (name == null) {
            val configName = configuration?.getString(Companion.INVENTORY_NAME_KEY)
            if (configName != null) title = configName
        } else {
            title = name;
        }

        container = if (name != null)
            Bukkit.createInventory(this, 9 * 4, name)
        else
            Bukkit.createInventory(this, 9 * 4)

        if (configuration != null) {

            val slotsSection = configuration.getConfigurationSection("slots")

            if (slotsSection != null) {
                val keys = slotsSection.getKeys(false)

                for (key in keys) {
                    val item = slotsSection.getItemStack(key)
                    if (item != null) inventory.setItem(key.toInt(), item)
                }

            }


        }


    }


    fun save(config: Configuration) {
        config.set(Companion.INVENTORY_NAME_KEY, name)

        val slots = config.createSection("slots")
        for (slot in 0 until inventory.size) {
            val item = inventory.getItem(slot)
            if (item == null || item.type != Material.AIR) continue
            slots.set(slot.toString(), item)
        }
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }


    override fun getInventory(): Inventory {
        return container
    }

    companion object {
        const val INVENTORY_NAME_KEY: String = "inventoryName"
    }

}
