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
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import java.util.*

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

    @Subcommand("cmdlist")
    @CommandPermission("alphheim.developer")
    fun cmdlist(sender: CommandSender) {
        val field = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
        field.isAccessible = true
        val commandMap: Map<String, Command> = field.get(plugin.server.commandMap) as Map<String, Command>

        val commands = TreeSet<Command>({ i1, i2 ->
            val name1 = when {
                i1 is PluginCommand -> i1.plugin.name + ":" + i1.name
                i1.javaClass.classLoader is PluginClassLoader -> (i1.javaClass.classLoader as PluginClassLoader).plugin.name + ":" + i1.name
                else -> {
                    i1.name
                }
            }

            val name2 = when {
                i2 is PluginCommand -> i2.plugin.name + ":" + i2.name
                i2.javaClass.classLoader is PluginClassLoader -> (i2.javaClass.classLoader as PluginClassLoader).plugin.name + ":" + i2.name
                else -> {
                    i2.name
                }
            }
            name1.compareTo(name2)

        })

        commandMap.entries.forEach { commands.add(it.value) }

        commands.forEach {
            val name = if (it is PluginCommand) {
                it.plugin.name + ":" + it.name
            } else {
                it.name
            }
            println("command: $name")
        }

    }
}


