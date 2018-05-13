/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandCore(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "alphheim") {
    val colorString: String


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
    fun fakevote(sender: CommandSender, @Single target: String, @Single address: String, @Single service: String) {
        plugin.voteHandler.createVote(target, address, service)
    }
}


