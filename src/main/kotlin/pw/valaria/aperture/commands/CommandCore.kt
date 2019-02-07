/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.spawn.SpawnHandler
import pw.valaria.aperture.components.tabfooterheader.TabHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader

// TODO: Remove reliance on alphheim
@CommandAlias("alphheim|core")
class CommandCore(private val plugin: ApertureCore) : CoreCommand(plugin) {
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

    @Subcommand("which")
    @CommandPermission("roles.admin")
    fun whichPlugin(sender: CommandSender, @Single commandName: String) {
        val command = plugin.server.commandMap.getCommand(commandName)
        if (command == null) {
            MessageUtil.sendError(sender, "Command does not exist!")
        } else {
            if (command is PluginIdentifiableCommand) {
                MessageUtil.sendInfo(sender, "Command $commandName belongs to ${command.plugin.name}")
                return
            } else {
                val classLoader = command.javaClass.classLoader
                if (classLoader is PluginClassLoader) {
                    MessageUtil.sendInfo(sender, "Command $commandName belongs to *${classLoader.plugin.name}")
                    return
                }
            }

        }

        MessageUtil.sendError(sender, "Command is not identifiable!")
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}


