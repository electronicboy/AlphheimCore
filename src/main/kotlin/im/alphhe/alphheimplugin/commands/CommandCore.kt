/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import java.util.*

@CommandAlias("alphheim")
class CommandCore(private val plugin: AlphheimCore) : AlphheimCommand(plugin) {
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
    fun reloadTab(sender: Player) {
        val tabHandler = plugin.tabHandler
        if (tabHandler == null) {
            MessageUtil.sendError(sender, "TabHandler is null?!")
        } else {
            tabHandler.reset()
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
        sender.inventory.addItem(plugin.spawnHandler.getBook())
    }

    @Subcommand("colors")
    @CommandAlias("colors")
    fun colors(sender: CommandSender) {
        MessageUtil.sendInfo(sender, colorString)
    }

    @Subcommand("fakevote")
    @CommandPermission("alphheim.developer")
    fun fakevote(@Single target: String, @Single address: String, @Single service: String) {
        plugin.voteHandler.createVote(target, address, service)
    }

    @Subcommand("toggleoverrides")
    @CommandPermission("alphheim.admin")
    fun toggleOverrides(sender: Player) {
        val user = plugin.userManager.getUser(sender.uniqueId)
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
        val user = if (target == null) {
            plugin.userManager.getUser(sender.uniqueId)
        } else {
            plugin.userManager.getUser(target.player.uniqueId)
        }
        val newMode = if (user.hasOverrides()) {
            "enabled"
        } else {
            "disabled"
        }
        MessageUtil.sendInfo(sender, "Staff overrides are $newMode for ${user.getNickname()}")
    }

}


