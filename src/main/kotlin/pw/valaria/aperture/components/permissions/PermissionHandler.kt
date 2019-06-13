/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.permissions

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableList
import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.Group
import me.lucko.luckperms.api.caching.MetaData
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import org.bukkit.util.StringUtil
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.health.HealthHandler
import pw.valaria.aperture.components.permissions.commands.CommandRank
import pw.valaria.aperture.components.racial.RacialHandler
import pw.valaria.aperture.components.tablist.TabListHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.data.DonorTier
import pw.valaria.aperture.utils.MySQL
import java.util.*
import java.util.concurrent.CompletionException

class PermissionHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private val userMetaCache: HashBasedTable<UUID, String, String> = HashBasedTable.create<UUID, String, String>(100, 20)
    private val commandRank: CommandRank

    init {
        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            if (!plugin.isEnabled) return@subscribe
            plugin.server.scheduler.runTask(plugin, Runnable {
                val player = plugin.server.getPlayer(it.user.uuid)
                if (player != null) {
                    plugin.componentHandler.getComponent(TabListHandler::class.java)?.setSB(player)
                    plugin.componentHandler.getComponent(HealthHandler::class.java)?.updateHealth(player)
                    plugin.componentHandler.getComponent(RacialHandler::class.java)?.applyEffects(player)

                    val user = plugin.componentHandler.getComponent(UserManager::class.java)?.getUser(it.user.uuid)
                    user?.setDisplayName(user.getNickname())
                }
            })
        }

        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            // clear the users cache row
            userMetaCache.row(it.user.uuid).clear()
        }

        plugin.commandManager.commandCompletions.registerAsyncCompletion("groups") {
            getGroups().filter { group -> StringUtil.startsWithIgnoreCase(group, it.input) }.toMutableSet()
        }

        commandRank = CommandRank(plugin) //temp extract
        plugin.commandManager.registerCommand(commandRank)

    }

    fun migrate(): Boolean {

        val uuids = mutableListOf<UUID>()

        MySQL.getConnection().use test@{ conn ->
            run {
                conn.prepareStatement("SELECT uuid FROM luckperms_players WHERE 1").use { ps ->
                    ps.executeQuery().use { rs ->

                        while (rs.next()) {
                            val stringUUID = rs.getString("uuid")
                            val uuid = UUID.fromString(stringUUID)
                            plugin.logger.info("got UUID: $stringUUID")
                            uuids.add(uuid)
                        }
                    }
                }
            }

        }


        uuids.forEach { uuid ->
            run {
                try {
                    commandRank.setRank(Bukkit.getConsoleSender(), uuid, "default")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        uuids.forEach { uuid ->
            var user = plugin.luckPermsApi.getUser(uuid)
            if (user == null) {
                user = plugin.luckPermsApi.userManager.loadUser(uuid).join()
            }

            if (user == null) {
                plugin.logger.warning("Could not attempt to fetch donar tier info for $uuid")
                return@forEach
            }

            DonorTier.values().reversedArray().forEach { tier ->
                run {
                    if (tier != DonorTier.NONE) {
                        getOwnGroupsForOfflineUser(uuid).forEach { group ->
                            if (group.name == tier.name.toLowerCase()) {
                                MySQL.getConnection().use { conn ->
                                    {
                                        conn.prepareStatement("UPDATE player_data SET PLAYER_DONATION_TIER = ? WHERE PLAYER_UUID = ?").use { ps ->
                                            {
                                                ps.setInt(1, tier.level)
                                                ps.setString(2, uuid.toString())
                                                if (ps.executeUpdate() == 0) {
                                                    plugin.logger.warning("FAILED TO TRANSLATE DATA FOR: $uuid")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }

        return true
    }

    override fun onDisable() {
        val iter = plugin.luckPermsApi.eventBus.getHandlers(UserDataRecalculateEvent::class.java).iterator()
        while (iter.hasNext()) {
            val handler = iter.next()
            if ((handler.consumer.javaClass.classLoader as PluginClassLoader).plugin == plugin) {
                handler.unregister()
            }
        }

    }

    fun getGroups(): Set<String> {
        return plugin.luckPermsApi.groups.filter {
            it.cachedData.getMetaData(Contexts.global()).meta.getOrDefault("persistSet", false) == true
        }.map { it.name }.toHashSet()
    }

    fun getGroup(group: String): Group? {
        return plugin.luckPermsApi.getGroup(group)
    }

    fun getGroupsForUser(player: Player): ImmutableList<Group> {
        val groups = ImmutableList.builder<Group>()
        val user = plugin.luckPermsApi.getUser(player.uniqueId) ?: return ImmutableList.of()

        for (resolveInheritance in user.resolveInheritances(Contexts.global())) {
            if (resolveInheritance.isGroupNode) {
                val group = getGroup(resolveInheritance.groupName) ?: continue
                groups.add(group)
            }
        }

        return groups.build()

    }

    fun getOwnGroupsForUser(player: Player): ImmutableList<Group> {
        val user = plugin.luckPermsApi.getUser(player.uniqueId) ?: return ImmutableList.of()

        val builder = ImmutableList.builder<Group>()
        user.ownNodes
                .filter { it.isGroupNode }
                .map { getGroup(it.groupName) }
                .forEach { if (it != null) builder.add(it) }

        return builder.build()
    }

    fun getOwnGroupsForOfflineUser(uuid: UUID): ImmutableList<Group> {
        var user = plugin.luckPermsApi.getUser(uuid)
        if (user == null) {
            user = plugin.luckPermsApi.userManager.loadUser(uuid).get()
        }

        if (user == null) return ImmutableList.of()

        val builder = ImmutableList.builder<Group>()
        user.ownNodes.filter { it.isGroupNode }.map { getGroup(it.groupName)!! }.forEach { builder.add(it) }

        return builder.build()

    }

    fun getBooleanMeta(group: Group, metaKey: String, default: Boolean = false): Boolean {
        val orDefault = group.cachedData.getMetaData(Contexts.global()).meta.getOrDefault(metaKey, default.toString())
        return orDefault?.toBoolean() ?: false

    }

    fun getLongMeta(player: Player, key: String, default: Long): Long {
        val groupsForUser = this.getGroupsForUser(player)
        var value: Long? = null
        for (group in groupsForUser) {
            val mappedValue = group.cachedData.getMetaData(Contexts.global()).meta[key]
            if (mappedValue != null) {
                try {
                    val long = mappedValue.toLong()
                    if (value == null) value = long
                } catch (ignored: NumberFormatException) {
                } // ignore...
            }

        }

        return value ?: default

    }


    fun getLongMetaCached(player: Player, key: String, default: Long): Long {
        val get = userMetaCache.get(player, key)
        return if (get == null) {
            val longMeta = getLongMeta(player, key, default)
            userMetaCache.put(player.uniqueId, key, longMeta.toString())
            longMeta
        } else {
            get.toLong()
        }
    }

    fun getMeta(player: Player, key: String, default: String): String {
        val user = plugin.luckPermsApi.getUser(player.uniqueId) ?: return default
        val meta = user.cachedData.getMetaData(plugin.luckPermsApi.getContextsForPlayer(player)).meta
        return meta[key] ?: default

    }

    fun getPlayerPrefix(player: Player, allowSlow: Boolean = false): String {
        val metadata = getMetadata(player, allowSlow)
        return metadata?.prefix ?: ""
    }

    fun getPlayerSuffix(player: Player, allowSlow: Boolean = false): String {
        val metadata = getMetadata(player, allowSlow)
        return metadata?.suffix ?: ""
    }

    fun getMetadata(player: Player, allowSlow: Boolean = false): MetaData? {
        var user = plugin.luckPermsApi.getUser(player.uniqueId)
        if (user == null) {
            if (allowSlow) {
                val loadUserFuture = plugin.luckPermsApi.userManager.loadUser(player.uniqueId)
                try {
                    user = loadUserFuture.join()
                } catch (ex: CompletionException) {
                    plugin.logger.warning("Failed to load user data for: $player")
                    ex.printStackTrace()
                }
            }
        }

        if (user == null) {
            return null
        }

        return user.cachedData.getMetaData(plugin.luckPermsApi.getContextsForPlayer(player))

    }

}