/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.permissions

import co.aikar.commands.BukkitCommandCompletionContext
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.permissions.commands.CommandRank
import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.Group
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import org.bukkit.util.StringUtil

class PermissionHandler(private val plugin: AlphheimCore) {

    val groups = HashMap<String, Group>()

    init {
        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java, {
            plugin.server.scheduler.runTask(plugin, {
                val player = this.plugin.server.getPlayer(it.user.uuid)
                if (player != null) {
                    plugin.tabListHandler.setSB(player)
                    plugin.healthHandler.updateHealth(player)
                    plugin.racialHandler.applyEffects(player)
                }
            })
        })

        plugin.commandManager.commandCompletions.registerCompletion("groups") { it: BukkitCommandCompletionContext ->
            val out = mutableSetOf<String>()
            for (group in getGroups()) {
                if (StringUtil.startsWithIgnoreCase(group, it.input)) out.add(group)
            }
            out
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

    fun getGroupsForUser(player: Player): MutableList<Group> {
        val groups = mutableListOf<Group>()
        for (group in getGroups()) {
            if (player.hasPermission("group.$group")) {
                groups.add(getGroup(group) ?: continue)
            }
        }
        return groups
    }

    fun getBooleanMeta(group: Group, metaKey: String, default: Boolean = false): Boolean {
        val orDefault = group.cachedData.getMetaData(Contexts.global()).meta.getOrDefault(metaKey, default.toString())
        return orDefault?.toBoolean() ?: false

    }


}