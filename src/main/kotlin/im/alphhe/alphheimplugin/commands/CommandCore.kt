/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import im.alphhe.alphheimplugin.components.tabfooterheader.TabHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("alphheim")
class CommandCore(private val plugin: EladriaCore) : AlphheimCommand(plugin) {
    private val colorString: String


    init {
        val sb = StringBuilder("Colors: ")
        for (color in ChatColor.values()) {
            if (color.isColor) {
                sb.append(color.toString()).append(color.char)
            }
        }
        colorString = sb.toString()
    }


    @CommandPermission("alphheim.admin")
    @Subcommand("reloadTab")
    fun reloadTab(sender: CommandSender) {
        val tabHandler = plugin.componentHandler.getComponent(TabHandler::class.java)
        if (tabHandler == null) {
            MessageUtil.sendError(sender, "TabHandler is null?!")
        } else {
            tabHandler.reset()
            MessageUtil.sendInfo(sender, "tab reset")
        }
    }

    @Subcommand("website")
    @CommandAlias("website")
    fun website(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "Visit our website @ http://alphhe.im")
    }


    @Subcommand("discord")
    @CommandAlias("discord")
    fun discord(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "Join us on Discord @ http://alphhe.im/discord")
    }

    @Subcommand("map")
    @CommandAlias("map")
    fun map(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "See your house @ http://map.alphhe.im/")
    }

    @Subcommand("spawnbook")
    fun spawnBook(sender: Player) {
        sender.inventory.addItem(plugin.componentHandler.getComponent(SpawnHandler::class.java)!!.getBook())
    }

    @Subcommand("colors")
    @CommandAlias("colors")
    fun colors(sender: CommandSender) {
        MessageUtil.sendInfo(sender, colorString)
    }

    @Subcommand("fakevote")
    @CommandPermission("alphheim.developer")
    fun fakevote(sender: CommandSender, @Single target: String, @Single address: String, @Single service: String) {
        val handler = plugin.componentHandler.getComponent(VoteHandler::class.java)
        if (handler != null) {
            handler.createVote(target, address, service)
        } else {
            MessageUtil.sendError(sender, "Vote handler is missing!")
        }

    }

    @Subcommand("toggleoverrides")
    @CommandPermission("alphheim.admin")
    fun toggleOverrides(sender: Player) {
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(sender.uniqueId)
        user.overrides = !user.hasOverrides()
        val newMode = if (user.hasOverrides()) {
            "enabled"
        } else {
            "disabled"
        }

        MessageUtil.broadcast("alphheim.admin", "${sender.name} has $newMode staff overrides!")
    }

    @Subcommand("checkoverrides")
    @CommandPermission("alphheim.admin")
    fun checkOverrides(sender: Player, @Optional target: OnlinePlayer?) {
        val userManager = plugin.componentHandler.getComponent(UserManager::class.java)!!
        val user = if (target == null) {
            userManager.getUser(sender.uniqueId)
        } else {
            userManager.getUser(target.player.uniqueId)
        }
        val newMode = if (user.hasOverrides()) {
            "enabled"
        } else {
            "disabled"
        }
        MessageUtil.sendInfo(sender, "Staff overrides are $newMode for ${user.getNickname()}")
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}


