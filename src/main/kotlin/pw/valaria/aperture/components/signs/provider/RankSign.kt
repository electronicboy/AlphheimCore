/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.provider

import com.google.common.cache.CacheBuilder
import me.lucko.luckperms.api.Contexts
import net.kyori.text.TextComponent
import net.kyori.text.format.TextColor
import net.kyori.text.serializer.legacy.LegacyComponentSerializer
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
        val lpapi = handler.plugin.luckPermsApi;

        if (!player.hasPermission("group.admin")) return

        val rankName = lines[1]
        val group = lpapi.getGroup(rankName)
        if (group == null) {
            MessageUtil.sendError(player, "This group does not exist?")
            return
        }

        val metadata = group.cachedData.getMetaData(Contexts.global())

        val persistMeta = metadata.meta["persistSet"]

        if (persistMeta == null || persistMeta.toBoolean()) {
            MessageUtil.sendError(player, "Unsupported group! (Does not persist?)")
        }

        sign.persistentDataContainer.set(handler.signKey, RankSignDataType.INSTANCE, RankSignDataType.RankSignData(rankName))
        sign.persistentDataContainer.set(handler.signTypeKey, PersistentDataType.STRING, providerName)


    }

    private val rankKey = NamespacedKey(handler.plugin, "rank")
    companion object {
        val lac = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build<UUID, Boolean>()
    }

    val header = "&6[&cRace Selection&6]".translateColors()

    override fun interact(player: Player, sign: Sign) {
        val interacted = lac.get(player.uniqueId) { false}

        if (!interacted) {
            MessageUtil.sendInfo(player, "Click again to confirm your selection")
            lac.put(player.uniqueId, true)
            return
        }
        // If we get this far, we shouldn't be null
        val data = sign.persistentDataContainer.get(handler.signKey, PersistentDataType.TAG_CONTAINER)!!
        val rank = data.get(rankKey, PersistentDataType.STRING) ?: return


        val permHandler = handler.plugin.componentHandler.getComponent(PermissionHandler::class.java)

        permHandler?.let {permissionHandler ->
            val group = permHandler.getGroup(rank)
            val targetBukkit = player

            val firstSet = player.hasPermission("group.default")

            if (group == null) {
                return
            }

            if (permHandler.getBooleanMeta(group, "persistSet")) {
                throw IllegalStateException("Group does not persist!")
            }


            var user = handler.plugin.luckPermsApi.getUser(player.uniqueId)
            if (user == null) {
                throw IllegalStateException("Offline player interacted with sign?! ${targetBukkit.uniqueId}|${targetBukkit.name}")
            }

            val context = handler.plugin.luckPermsApi.getContextsForPlayer(player)
            val metadata = user.cachedData.getMetaData(context)

            metadata.meta.get("raceselected")
            val groups = user.ownNodes.filter { it.isGroupNode }.map { it.groupName }.toMutableList()
            val toRemove = mutableListOf<String>()
            groups.forEach {
                val groupIn = permHandler.getGroup(it)
                if (groupIn == null || !permHandler.getBooleanMeta(groupIn, "persistSet")) toRemove.add(it)
            }

            for (remove in toRemove) {
                user.unsetPermission(handler.plugin.luckPermsApi.nodeFactory.makeGroupNode(remove).build())
            }

            user.setPermission(handler.plugin.luckPermsApi.nodeFactory.makeGroupNode(group).build())
            user.primaryGroup = group.name

            handler.plugin.luckPermsApi.userManager.saveUser(user).thenRunAsync { user.refreshCachedData() }


            if (!firstSet) return

            val inventory = if (targetBukkit.isOnline) { targetBukkit.player!!.inventory} else { return}
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

            handler.plugin.componentHandler.getComponent(SpawnHandler::class.java)?.let {spawnHandler ->
                spawnHandler.goSpawn(targetBukkit, null, true)
            }

        }



    }

    override fun render(sign: Sign): List<String> {
        val signData = sign.persistentDataContainer.get(handler.signKey, RankSignDataType.INSTANCE) ?: throw IllegalStateException("Called render on sign missing data?")

        val prefix = handler.plugin.luckPermsApi.getGroup(signData.rank)?.cachedData?.getMetaData(Contexts.global())?.prefix ?: signData.rank
        val lines = ArrayList<String>()


        lines+ header
        lines+ prefix
        lines+ ""
        lines+ ""

        return lines
    }


}