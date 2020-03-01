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
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.event.user.UserDataRecalculateEvent
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.query.QueryOptions
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import org.bukkit.util.StringUtil
import org.checkerframework.checker.nullness.qual.NonNull
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.health.HealthHandler
import pw.valaria.aperture.components.permissions.commands.CommandRank
import pw.valaria.aperture.components.racial.RacialHandler
import pw.valaria.aperture.components.tablist.TabListHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

class PermissionHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private val userMetaCache: HashBasedTable<UUID, String, String> = HashBasedTable.create<UUID, String, String>(100, 20)
    private val commandRank: CommandRank
    private val luckPerms: LuckPerms

    init {
        luckPerms = LuckPermsProvider.get();
        luckPerms.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            if (!plugin.isEnabled) return@subscribe
            plugin.server.scheduler.runTask(plugin, Runnable {
                val player = plugin.server.getPlayer(it.user.uniqueId)
                if (player != null) {
                    plugin.componentHandler.getComponent(TabListHandler::class.java)?.setSB(player)
                    plugin.componentHandler.getComponent(HealthHandler::class.java)?.updateHealth(player)
                    plugin.componentHandler.getComponent(RacialHandler::class.java)?.applyEffects(player)

                    val user = plugin.componentHandler.getComponent(UserManager::class.java)?.getUser(it.user.uniqueId)
                    user?.setDisplayName(user.getNickname())
                }
            })
        }

        luckPerms.eventBus.subscribe(UserDataRecalculateEvent::class.java) {
            // clear the users cache row
            userMetaCache.row(it.user.uniqueId).clear()
        }

        plugin.commandManager.commandCompletions.registerAsyncCompletion("groups") {
            getGroups().filter { group -> StringUtil.startsWithIgnoreCase(group, it.input) }.toMutableSet()
        }

        commandRank = CommandRank(plugin) //temp extract
        plugin.commandManager.registerCommand(commandRank)

    }

    override fun onDisable() {
        val iter = luckPerms.eventBus.getSubscriptions(UserDataRecalculateEvent::class.java).iterator()
        while (iter.hasNext()) {
            val handler = iter.next()
            if ((handler.handler.javaClass.classLoader as PluginClassLoader).plugin == plugin) {
                handler.close()
            }
        }

    }

    fun getGroups(): Set<String> {
        return luckPerms.groupManager.loadedGroups.filter {
            it.cachedData.getMetaData(QueryOptions.nonContextual()).getMetaValue("persistSet")?.toBoolean() ?: false
        }.map { it.name }.toHashSet()
    }

    fun getGroup(group: String): Group? {
        return luckPerms.groupManager.getGroup(group)
    }

    fun getGroupsForUser(player: Player): ImmutableList<Group> {
        val groups = ImmutableList.builder<Group>()
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return ImmutableList.of()

        for (resolveInheritance in user.resolveInheritedNodes(QueryOptions.defaultContextualOptions())) {
            if (resolveInheritance.type == NodeType.INHERITANCE) {

                val group = getGroup(resolveInheritance.key.removePrefix("group.")) ?: continue
                groups.add(group)
            }
        }

        return groups.build()

    }

    fun getOwnGroupsForUser(player: Player): ImmutableList<Group> {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return ImmutableList.of()

        val builder = ImmutableList.builder<Group>()
        user.nodes.filter { it.type == NodeType.INHERITANCE }
                .map { getGroup(it.key.removePrefix("group.")) }
                .forEach { if (it != null) builder.add(it) }

        return builder.build()
    }

    fun getOwnGroupsForOfflineUser(uuid: UUID): ImmutableList<Group> {
        var user = luckPerms.userManager.getUser(uuid)
        if (user == null) {
            user = luckPerms.userManager.loadUser(uuid).get()
        }

        if (user == null) return ImmutableList.of()

        val builder = ImmutableList.builder<Group>()
        user.nodes.filter { it is InheritanceNode }
                .map { (it as InheritanceNode).groupName }
                .forEach { if (it != null) builder.add(getGroup(it)) }

        return builder.build()

    }

    fun getBooleanMeta(group: Group, metaKey: String, default: Boolean = false): Boolean {
        val value = group.cachedData.getMetaData(QueryOptions.defaultContextualOptions()).getMetaValue(metaKey);
        return value?.toBoolean() ?: default

    }

    fun getLongMeta(player: Player, key: String, default: Long): Long {
        val groupsForUser = this.getGroupsForUser(player)
        var value: Long? = null
        for (group in groupsForUser) {
            val mappedValue = group.cachedData.getMetaData(QueryOptions.defaultContextualOptions()).getMetaValue(key)
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
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return default
        val meta = user.cachedData.getMetaData(QueryOptions.contextual(luckPerms.contextManager.getContext(user).get())).getMetaValue(key)
        return meta ?: default

    }

    fun getPlayerPrefix(player: Player, allowSlow: Boolean = false): String {
        val metadata = getMetadata(player, allowSlow)
        return metadata?.prefix ?: ""
    }

    fun getPlayerSuffix(player: Player, allowSlow: Boolean = false): String {
        val metadata = getMetadata(player, allowSlow)
        return metadata?.suffix ?: ""
    }

    fun getMetadata(player: Player, allowSlow: Boolean = false): CachedMetaData? {
        var user = luckPerms.userManager.getUser(player.uniqueId)
        if (user == null) {
            if (allowSlow) {
                val loadUserFuture = luckPerms.userManager.loadUser(player.uniqueId)
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

        return user.cachedData.getMetaData(QueryOptions.contextual(luckPerms.contextManager.getContext(user).get()))

    }

    fun getUser(target: UUID, allowSlow: Boolean = false): User? {
        var user = luckPerms.userManager.getUser(target)
        if (user == null) {
            if (allowSlow) {
                val loadUserFuture = luckPerms.userManager.loadUser(target)
                try {
                    user = loadUserFuture.join()
                } catch (ex: CompletionException) {
                    plugin.logger.warning("Failed to load user data for: $target")
                    ex.printStackTrace()
                }
            }
        }
        return user;
    }

    fun unsetPermission(target: UUID, remove: Group) {
        val user = getUser(target, true)!!
        user.data().remove(luckPerms.nodeBuilderRegistry.forInheritance().group(remove).build())
    }

    fun saveUser(target: UUID): @NonNull CompletableFuture<Void> {
        val user = luckPerms.userManager.getUser(target)
        if (user == null) throw IllegalStateException("$target is not loaded!")
        return luckPerms.userManager.saveUser(user);
    }

    fun refreshForUserIfOnline(target: UUID) {
        val user = getUser(target) ?: return
        user.cachedData.invalidate();
    }

    fun addGroup(target: UUID, group: Group) {
        val user = getUser(target, true)!!
        user.data().add(luckPerms.nodeBuilderRegistry.forInheritance().group(group).build())
    }

}