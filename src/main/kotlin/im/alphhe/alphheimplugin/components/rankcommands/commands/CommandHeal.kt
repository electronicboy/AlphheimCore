/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands.commands

import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandHeal(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "aheal") {

    @Subcommand("heal")
    @CommandAlias("heal")
    @CommandPermission("alphheim.heal")
    @CommandCompletion("@players")
    fun onHeal(sender: Player, @Optional target: OnlinePlayer?) {
        if (checkCooldown(sender, "healCooldown")) {
            if (target == null) {
                sender.health = sender.maxHealth
            } else {
                target.player.health = target.player.maxHealth
            }
            MessageUtil.sendInfo(sender, "The spell was casted successfully!")

        }
    }

    @Subcommand("radius")
    @CommandAlias("rheal")
    @CommandPermission("alphheim.heal.radius")
    fun onRadiusHeal(sender: Player) {
        if (checkCooldown(sender, "healCooldown")) {
            val location = sender.location
            for (nearbyEntity in location.world.getNearbyEntities(location, 7.0, 7.0, 7.0)) {
                if (nearbyEntity is Player) {
                    nearbyEntity.health = nearbyEntity.maxHealth
                }
            }
            MessageUtil.sendInfo(sender, "The spell was casted successfully!")
        }
    }

    @Subcommand("force")
    @CommandAlias("fheal")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    fun onStaffHeal(sender: CommandSender, @Optional target: OnlinePlayer?) {
        when {
            target != null -> {
                target.player.foodLevel = 20
                target.player.saturation = 20f
                target.player.health = target.player.maxHealth
                MessageUtil.sendInfo(sender, "You have been healed")
            }
            sender is Player -> {
                sender.foodLevel = 20
                sender.player.saturation = 20f
                sender.health = sender.maxHealth
                MessageUtil.sendInfo(sender, "You have been healed")
            }
            else -> MessageUtil.sendError(sender, "Missing target?!")
        }

    }
}

