/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import net.md_5.bungee.api.chat.ComponentBuilder
import net.minecraft.server.v1_8_R3.ChatBaseComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AlphheimCoreCommand(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "alphheim") {


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
        MessageUtil.sendInfo(sender, "http://alphhe.im")
    }


    @Subcommand("discord")
    @CommandAlias("discord")
    fun discord(sender: CommandSender) {
        MessageUtil.sendInfo(sender, "http://alphhe.im/discord")
    }
}


