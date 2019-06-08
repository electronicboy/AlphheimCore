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
import co.aikar.commands.annotation.Optional
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.utils.MessageUtil
import me.lucko.luckperms.api.Group
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
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
    fun setRank(sender: CommandSender, target: OnlinePlayer, @Single rank: String, @Optional @Default("false") firstSet: Boolean) {
        setRank(sender, target.player.uniqueId, rank, firstSet)

    }

    fun setRank(sender: CommandSender, target: UUID, @Single rank: String, @Optional @Default("false") firstSet: Boolean) {
        val permHandler = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!
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


        var user = plugin.luckPermsApi.getUser(target)
        if (user == null) {
            MessageUtil.sendError(sender, "Error occured fetching profile for ${targetBukkit.uniqueId}|${targetBukkit.name}")
            //return TEMP DISABLE...
        }

        if (user == null) {
            user = plugin.luckPermsApi.userManager.loadUser(target).join()
        }

        if (user == null) {
            MessageUtil.sendError(sender, "Still missing?!")
            return
        }

        val groups = user.ownNodes.filter { it.isGroupNode }.map { it.groupName }.toMutableList()
        val toRemove = mutableListOf<String>()
        groups.forEach {
            val groupIn = permHandler.getGroup(it)
            if (groupIn == null || !permHandler.getBooleanMeta(groupIn, "persistSet")) toRemove.add(it)
        }

        for (remove in toRemove) {
            user.unsetPermission(plugin.luckPermsApi.nodeFactory.makeGroupNode(remove).build())
        }

        user.setPermission(plugin.luckPermsApi.nodeFactory.makeGroupNode(group).build())
        user.primaryGroup = group.name

        plugin.luckPermsApi.userManager.saveUser(user).thenRunAsync { user.refreshCachedData() }.thenRunAsync {
            MessageUtil.sendInfo(sender, "Added group ${group.name} to ${user.name}; All set: ${user.ownNodes.filter { it.isGroupNode }.map { it.groupName }}")
        }
    }

    @CommandPermission("group.mod")
    @Subcommand("inspect|info|i")
    @CommandCompletion("@players")
    fun inspect(sender: CommandSender, target: OnlinePlayer) {
        val user = plugin.luckPermsApi.getUser(target.player.uniqueId)
        if (user == null) {
            MessageUtil.sendError(sender, "Error occured fetching profile for ${target.player.name}")
            return
        }

        MessageUtil.sendInfo(sender, "groups set for user set: ${user.ownNodes.filter { it.isGroupNode }.map { it.groupName }}")
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}