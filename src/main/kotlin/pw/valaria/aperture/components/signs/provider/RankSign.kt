/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.provider

import com.google.common.cache.CacheBuilder
import net.luckperms.api.model.group.Group
import net.luckperms.api.query.QueryOptions
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.signs.SignHandler
import pw.valaria.aperture.components.signs.data.RankSignDataType
import pw.valaria.aperture.components.spawn.SpawnHandler
import pw.valaria.aperture.translateColors
import pw.valaria.aperture.utils.MessageUtil
import java.util.*
import java.util.concurrent.TimeUnit

class RankSign(handler: SignHandler) : AbstractSign(handler, "rank") {
    override fun create(player: Player, sign: Sign, lines: List<String>) {

        if (!player.hasPermission("group.admin")) return

        val rankName = lines[1]
        val permissionHandler = handler.plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)
        val group = permissionHandler.getGroup(rankName)
        if (group == null) {
            MessageUtil.sendError(player, "This group does not exist?")
            return
        }

        val metadata = group.cachedData.getMetaData(QueryOptions.defaultContextualOptions())

        val persistMeta = metadata.getMetaValue("persistSet")

        if (persistMeta == null || persistMeta.toBoolean()) {
            MessageUtil.sendError(player, "Unsupported group! (Does not persist?)")
        }

        handler.plugin.server.scheduler.runTask(handler.plugin, Runnable {
            val newSign = sign.block.state as? Sign
            if (newSign != null) {
                newSign.persistentDataContainer.set(handler.signTypeKey, PersistentDataType.STRING, providerName)
                newSign.persistentDataContainer.set(handler.signKey, RankSignDataType.INSTANCE, RankSignDataType.RankSignData(group.name))
                newSign.update()

            }

        })

    }

    private val rankKey = NamespacedKey(handler.plugin, "rank")

    companion object {
        val lac = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build<UUID, Boolean>()
    }

    val header = "&6[&cRace Selection&6]".translateColors()

    override fun interact(player: Player, sign: Sign) {
        val interacted = lac.get(player.uniqueId) { false }

        if (!interacted) {
            MessageUtil.sendInfo(player, "Click again to confirm your selection")
            lac.put(player.uniqueId, true)
            return
        }
        // If we get this far, we shouldn't be null
        val data = sign.persistentDataContainer.get(handler.signKey, RankSignDataType.INSTANCE)!!
        val rank = data.rank


        val permHandler = handler.plugin.componentHandler.getComponent(PermissionHandler::class.java)

        permHandler?.let { permissionHandler ->
            val group = permissionHandler.getGroup(rank)
            val targetBukkit = player

            val firstSet = !(player.hasPermission("race.human") || player.hasPermission("race.elf") || player.hasPermission("race.dwarf"))

            if (group == null) {
                return
            }

            if (permissionHandler.getBooleanMeta(group, "persistSet")) {
                throw IllegalStateException("Group does not persist!")
            }


            val groupsForOfflineUser = permHandler.getOwnGroupsForOfflineUser(player.uniqueId)

            val toRemove = mutableListOf<Group>()
            groupsForOfflineUser.forEach {
                if (!permHandler.getBooleanMeta(it, "persistSet")) toRemove.add(it)
            }

            for (remove in toRemove) {
                permHandler.unsetPermission(player.uniqueId, remove)
            }
            permHandler.saveUser(player.uniqueId).thenRunAsync {permHandler.refreshForUserIfOnline(player.uniqueId)}


            if (!firstSet) return

            val inventory = if (targetBukkit.isOnline) {
                targetBukkit.player!!.inventory
            } else {
                return
            }
            if (inventory.helmet == null && inventory.chestplate == null && inventory.leggings == null && inventory.boots == null) {

                inventory.helmet = ItemStack(Material.CHAINMAIL_HELMET)
                inventory.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE)
                inventory.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS)
                inventory.boots = ItemStack(Material.CHAINMAIL_BOOTS)
            }

            inventory.addItem(
                    ItemStack(Material.STONE_SWORD),
                    ItemStack(Material.STONE_AXE),
                    ItemStack(Material.STONE_PICKAXE),
                    ItemStack(Material.STONE_SHOVEL),
                    ItemStack(Material.STONE_HOE),
                    ItemStack(Material.COOKED_BEEF, 16),
                    ItemStack(Material.COMPASS),
                    ItemStack(Material.ACACIA_BOAT)
            )

            handler.plugin.componentHandler.getComponent(SpawnHandler::class.java)?.let { spawnHandler ->
                spawnHandler.goSpawn(targetBukkit, null, true)
            }

        }


    }

    override fun render(sign: Sign): List<String> {
        val signData = sign.persistentDataContainer.get(handler.signKey, RankSignDataType.INSTANCE)
                ?: throw IllegalStateException("Called render on sign missing data?")

        val prefix = handler.plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java).getGroup(signData.rank)?.cachedData?.getMetaData(QueryOptions.defaultContextualOptions())?.prefix
                ?: signData.rank
        val lines = ArrayList<String>()

        lines.add(header)
        lines.add(prefix.translateColors())
        lines.add("")
        lines.add("")

        return lines
    }


}