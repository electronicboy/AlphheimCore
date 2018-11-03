/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.permissions.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import me.lucko.luckperms.api.Group
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

@CommandAlias("rank")
class CommandRank(private val plugin: EladriaCore) : AlphheimCommand(plugin) {

    @Default
    @Subcommand("list|l")
    @CommandPermission("alphheim.mod")
    fun listRanks(sender: CommandSender) {
        sender.sendMessage(plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getGroups().joinToString(","))
    }

    @Subcommand("set|s")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players @groups")
    fun setRank(sender: CommandSender, target: OnlinePlayer, @Single rank: String, @Optional @Default("false") firstSet: Boolean) {
        val permHandler = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!
        val group = permHandler.getGroup(rank)
        if (group == null) {
            MessageUtil.sendError(sender, "Rank $rank does not exist!")
            return
        }

        if (permHandler.getBooleanMeta(group, "persistSet")) {
            MessageUtil.sendError(sender, "This is NOT a user rank, see `/lp user <user> parent add <group>` to add specialised ranks!")

            return
        }


        val user = plugin.luckPermsApi.getUser(target.player.uniqueId)
        if (user == null) {
            MessageUtil.sendError(sender, "Error occured fetching profile for ${target.player.name}")
            return
        }

        val groups = user.ownNodes.filter { it.isGroupNode }.map { permHandler.getGroup(it.groupName)!! }.toMutableList()
        val toRemove = mutableListOf<Group>()
        groups.forEach {
            if (!permHandler.getBooleanMeta(it, "persistSet")) toRemove.add(it)
        }

        for (remove in toRemove) {
            user.unsetPermission(plugin.luckPermsApi.nodeFactory.makeGroupNode(remove).build())
        }

        user.setPermission(plugin.luckPermsApi.nodeFactory.makeGroupNode(group).build())
        user.primaryGroup = group.name

        plugin.luckPermsApi.userManager.saveUser(user).thenRunAsync { user.refreshCachedData() }.thenRunAsync {
            MessageUtil.sendInfo(sender, "Added group ${group.name}; All set: ${user.ownNodes.filter { it.isGroupNode }.map { it.groupName }}")
        }

        if (!firstSet) return

        val inventory = target.player.inventory
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

        target.player.teleport(Location(Bukkit.getWorlds()[0], 850.0, 37.0, -1696.0, 180f, 0f))


    }

    @CommandPermission("alphheim.mod")
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