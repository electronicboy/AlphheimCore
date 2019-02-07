/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta

class MobSpawnerHandler() : IDonorHandler() {
    override val name: String
        get() = "MobSpawner"

    override fun handle(player: Player, args: Map<String, String>) {
        val spawnerType = args["spawnerType"]
                ?: throw IllegalArgumentException("Missing spawner type!!")

        @Suppress("DEPRECATION") val entityType = EntityType.fromName(spawnerType)
        @Suppress("DEPRECATION")
        if (entityType == null) throw IllegalArgumentException("invalid spawner type!!")

        val items = player.inventory.addItem(getSpawner(entityType, args["displayName"]))
        for (item in items) {
            player.location.world.dropItemNaturally(player.location, item.value)
        }
    }

    private fun getSpawner(mobType: EntityType, name: String?): ItemStack {

        val item = ItemStack(Material.SPAWNER)
        val itemmeta = item.itemMeta as BlockStateMeta

        val spawner = itemmeta.blockState as CreatureSpawner
        spawner.spawnedType = mobType

        itemmeta.blockState = spawner

        itemmeta.displayName = if (name != null) name  else mobType.name
        item.itemMeta = itemmeta

        return item;

    }
}