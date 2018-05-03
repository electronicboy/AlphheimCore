/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.handlers

import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.minecraft.server.v1_8_R3.NBTTagList
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MobSpawnerHandler() : IDonorHandler() {
    override val name: String
        get() = "MobSpawner"

    override fun handle(player: Player, args: Map<String, String>) {
        val spawnerType = args["spawnerType"]
                ?: throw IllegalArgumentException("Missing spawner type!!")

        @Suppress("DEPRECATION")
        if (EntityType.fromName(spawnerType) == null) throw IllegalArgumentException("invalid spawner type!!")

        val items = player.inventory.addItem(getSpawner(spawnerType, args["displayName"]))
        for (item in items) {
            player.location.world.dropItemNaturally(player.location, item.value)
        }
    }

    private fun getSpawner(mobType: String, name: String?): ItemStack {

        val stack = CraftItemStack.asNMSCopy(ItemStack(Material.MOB_SPAWNER))

        val tag = stack.tag ?: NBTTagCompound()

        val blockEntityTag = if (tag.hasKeyOfType("BlockEntityTag", 10)) {
            tag.getCompound("BlockEntityTag")
        } else {

            NBTTagCompound()
        }

        blockEntityTag.setString("id", "MobSpawner")
        blockEntityTag.setString("EntityId", mobType)

        val spawnData = NBTTagCompound()
        spawnData.setString("id", mobType)

        blockEntityTag.set("SpawnData", spawnData)
        blockEntityTag.set("SpawnPotentials", NBTTagList())

        tag.set("BlockEntityTag", blockEntityTag)

        stack.tag = tag;

        val bukkitStack = CraftItemStack.asCraftMirror(stack)

        if (name != null) {
            val meta = bukkitStack.itemMeta
            meta.displayName = name.replace("_", " ")
            bukkitStack.itemMeta = meta
        }

        return bukkitStack
    }
}