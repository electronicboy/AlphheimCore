/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.permissions

import co.aikar.commands.BukkitCommandCompletionContext
import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableList
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.health.HealthHandler
import im.alphhe.alphheimplugin.components.permissions.commands.CommandRank
import im.alphhe.alphheimplugin.components.racial.RacialHandler
import im.alphhe.alphheimplugin.components.tablist.TabListHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.Group
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import org.bukkit.util.StringUtil
import java.util.*

class PermissionHandler(plugin: AlphheimCore) : AbstractHandler(plugin) {

    private val userMetaCache: HashBasedTable<UUID, String, String> = HashBasedTable.create<UUID, String, String>(100, 20)

    init {
        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            plugin.server.scheduler.runTask(plugin) {
                val player = this.plugin.server.getPlayer(it.user.uuid)
                if (player != null) {
                    plugin.componentHandler.getComponent(TabListHandler::class.java)?.setSB(player)
                    plugin.componentHandler.getComponent(HealthHandler::class.java)?.updateHealth(player)
                    plugin.componentHandler.getComponent(RacialHandler::class.java)?.applyEffects(player)

                    val user = plugin.componentHandler.getComponent(UserManager::class.java)?.getUser(it.user.uuid)
                    user?.setDisplayName(user.getNickname())
                }
            }
        }

        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            // clear the users cache row
            userMetaCache.row(it.user.uuid).clear()
        }

        plugin.commandManager.commandCompletions.registerCompletion("groups") { it: BukkitCommandCompletionContext ->
            getGroups().filter { group -> StringUtil.startsWithIgnoreCase(group, it.input) }.toMutableSet()
        }

        plugin.commandManager.registerCommand(CommandRank(plugin))

    }

    fun destruct() {
        val iter = plugin.luckPermsApi.eventBus.getHandlers(UserDataRecalculateEvent::class.java).iterator();
        while (iter.hasNext()) {
            val handler = iter.next()
            if ((handler.consumer.javaClass.classLoader as PluginClassLoader).plugin.name == "AlphheimCore") {
                handler.unregister()
            }
        }

    }

    fun getGroups(): Set<String> {
        return plugin.luckPermsApi.groups.map { it.name }.toHashSet()
    }

    fun getGroup(group: String): Group? {
        return plugin.luckPermsApi.getGroup(group)
    }

    fun updateGroup(player: OfflinePlayer, rank: String): Boolean {
        val user = this.plugin.luckPermsApi.userManager.getUser(player.uniqueId)
                ?: throw IllegalArgumentException("Attempted to set rank ($rank) for ${player.name} but we didn't find them!")

        return true
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
        user.ownNodes.filter { it.isGroupNode }.map { getGroup(it.groupName)!! }.forEach { builder.add(it) }

        return builder.build();
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
        val meta = user.cachedData.getMetaData(Contexts.global()).meta
        return meta[key] ?: default

    }


}