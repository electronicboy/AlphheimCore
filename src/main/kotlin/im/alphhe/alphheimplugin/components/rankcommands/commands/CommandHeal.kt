/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import org.bukkit.entity.Player

class CommandHeal(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "aheal") {

    @Subcommand("heal")
    @CommandAlias("heal")
    @CommandPermission("alphheim.heal")
    fun onHeal(sender: Player, @Optional target: OnlinePlayer) {

    }



}