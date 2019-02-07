/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.rankcommands.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("aheal")
class CommandHeal(plugin: ApertureCore) : CoreCommand(plugin) {

    @Subcommand("heal")
    @CommandAlias("heal")
    @CommandPermission("alphheim.heal")
    @CommandCompletion("@players")
    @Description("Heal the targeted player (or yourself)")
    fun onHeal(sender: Player, @Optional target: OnlinePlayer?) {
        if (checkCooldown(sender, "healCooldown")) {
            if (target == null) {
                sender.health = sender.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
            } else {
                target.player.health = target.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
            }
            MessageUtil.sendInfo(sender, "The spell was casted successfully!")

        }
    }

    @Subcommand("radius")
    @CommandAlias("rheal")
    @CommandPermission("alphheim.heal.radius")
    @Description("Heal all players in a 7m radius")
    fun onRadiusHeal(sender: Player) {
        if (checkCooldown(sender, "healCooldown")) {
            val location = sender.location
            for (nearbyEntity in location.world.getNearbyEntities(location, 7.0, 7.0, 7.0)) {
                if (nearbyEntity is Player) {
                    nearbyEntity.health = nearbyEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
                }
            }
            MessageUtil.sendInfo(sender, "The spell was casted successfully!")
        }
    }

    @Subcommand("force")
    @CommandAlias("fheal")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    @Description("staff force heal command")
    fun onStaffHeal(sender: CommandSender, @Optional target: OnlinePlayer?) {
        when {
            target != null -> {
                target.player.foodLevel = 20
                target.player.saturation = 20f
                target.player.health = target.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
                MessageUtil.sendInfo(target.player, "You have been healed")
                MessageUtil.sendInfo(sender, "You have healed ${target.player.name}")

            }
            sender is Player -> {
                sender.foodLevel = 20
                sender.player.saturation = 20f
                sender.health = sender.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
                MessageUtil.sendInfo(sender, "You have been healed")
            }
            else -> MessageUtil.sendError(sender, "Missing target?!")
        }

    }

    @Subcommand("feed")
    @CommandAlias("feed")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    @Description("staff force heal command")
    fun onFeed(sender: CommandSender, @Optional target: OnlinePlayer?) {
        when {
            target != null -> {
                target.player.foodLevel = 20
                target.player.saturation = 20f
                MessageUtil.sendInfo(sender, "You feel as if you've had the feast of a king!")
            }
            sender is Player -> {
                sender.foodLevel = 20
                sender.player.saturation = 20f
                MessageUtil.sendInfo(sender, "You feel as if you've had the feast of a king!")
            }
            else -> MessageUtil.sendError(sender, "Missing target?!")
        }

    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }

}

