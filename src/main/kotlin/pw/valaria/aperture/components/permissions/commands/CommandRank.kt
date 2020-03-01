/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.permissions.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import net.luckperms.api.model.group.Group
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.utils.MessageUtil
import java.util.*

@CommandAlias("rank")
class CommandRank(private val plugin: ApertureCore) : CoreCommand(plugin) {

    @Default
    @Subcommand("list|l")
    @CommandPermission("group.mod")
    fun listRanks(sender: CommandSender) {
        sender.sendMessage(plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getGroups().joinToString(","))
    }

    @Subcommand("set|s")
    @CommandPermission("group.mod")
    @CommandCompletion("@players @groups")
    fun setRank(sender: CommandSender, target: OnlinePlayer, @Single rank: String) {
        setRank(sender, target.player.uniqueId, rank)

    }

    fun setRank(sender: CommandSender, target: UUID, @Single rank: String) {
        val permHandler = plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)
        val group = permHandler.getGroup(rank)
        val targetBukkit = Bukkit.getOfflinePlayer(target)

        if (group == null) {
            MessageUtil.sendError(sender, "Rank $rank does not exist!")
            return
        }

        if (permHandler.getBooleanMeta(group, "persistSet")) {
            MessageUtil.sendError(sender, "This is NOT a user rank, see `/lp user <user> parent add <group>` to add specialised ranks!")

            return
        }


        val groupsForOfflineUser = permHandler.getOwnGroupsForOfflineUser(target)

        val toRemove = mutableListOf<Group>()
        groupsForOfflineUser.forEach {
            if (!permHandler.getBooleanMeta(it, "persistSet")) toRemove.add(it)
        }

        for (remove in toRemove) {
            permHandler.unsetPermission(target, remove)
        }

        permHandler.addGroup(target, group)

        permHandler.saveUser(target).thenRunAsync {permHandler.refreshForUserIfOnline(target)} .thenRunAsync {
            MessageUtil.sendInfo(sender, "Added group ${group.name} to ${targetBukkit.name}; All set: ${permHandler.getOwnGroupsForOfflineUser(target).map { it.name }}")
        }


    }

    @CommandPermission("group.mod")
    @Subcommand("inspect|info|i")
    @CommandCompletion("@players")
    fun inspect(sender: CommandSender, target: OnlinePlayer) {
        val permHandler = plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)

        MessageUtil.sendInfo(sender, "groups set for user set: ${permHandler.getOwnGroupsForOfflineUser(target.player.uniqueId).map { it.name }}")
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}