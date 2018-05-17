/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.permissions.commands

import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import me.lucko.luckperms.api.Group
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class CommandRank(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "rank") {

    private val worldSpawn = Location(Bukkit.getWorlds()[0], 850.0, 37.0, -1696.0, 180f, 0f)

    @Default
    @Subcommand("list")
    @CommandPermission("alphheim.mod")
    fun listRanks(sender: CommandSender) {
        sender.sendMessage(plugin.permissionHandler.getGroups().joinToString(","))
    }

    @Subcommand("set")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players @groups")
    fun setRank(sender: CommandSender, target: OnlinePlayer, @Single rank: String, @Optional @Default("false") firstSet: Boolean) {
        val permHandler = plugin.permissionHandler
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

        plugin.luckPermsApi.userManager.saveUser(user).thenRunAsync({ user.refreshCachedData() }).thenRunAsync({
            MessageUtil.sendInfo(sender, "Added group ${group.name}; All set: ${user.ownNodes.filter { it.isGroupNode }.map { it.groupName }}")
        })

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
                ItemStack(Material.STONE_SPADE),
                ItemStack(Material.STONE_HOE),
                ItemStack(Material.COOKED_BEEF, 16),
                ItemStack(Material.COMPASS),
                ItemStack(Material.BOAT)
        )

        target.player.teleport(worldSpawn)


    }
}