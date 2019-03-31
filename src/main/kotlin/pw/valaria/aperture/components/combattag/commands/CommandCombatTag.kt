/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.combattag.CombatTagHandler
import pw.valaria.aperture.toRemainingString
import pw.valaria.aperture.utils.MessageUtil
import java.time.Duration


@CommandAlias("ct|combattag")
class CommandCombatTag(plugin: ApertureCore, private val handler: CombatTagHandler) : CoreCommand(plugin) {



    @Subcommand("check")
    fun check(issuer: CommandSender) {
        if (issuer !is Player) {
            MessageUtil.sendError(issuer, "This command is only available to players, try specifying a player")
            return
        }

        val tag = handler.getTag(issuer)
        if (tag == null) {
            MessageUtil.sendInfo(issuer, "You are not tagged!")
        } else {
            val remaining = tag.getRemainingDuration()
            MessageUtil.sendInfo(issuer, "You are currently tagged, ${remaining.toRemainingString()} remaining!")
        }
    }

    @Subcommand("check")
    @CommandPermission("alphheim.admin")
    @CommandCompletion("@players")
    fun check(issuer: CommandSender, target: OnlinePlayer) {


        val tag = handler.getTag(target.player.uniqueId)

        if (tag == null) {
            MessageUtil.sendInfo(issuer, "${target.player.name} is not tagged!")
        } else {
            val remaining = tag.getRemainingDuration()
            MessageUtil.sendInfo(issuer, "${target.player.name} is currently tagged, ${remaining.toRemainingString()} remaining!")
        }
    }

    @Subcommand("set|tag")
    @CommandPermission("alphheim.admin")
    @CommandCompletion("@players")
    fun set(issuer: CommandSender, target: OnlinePlayer, duration: Long?) {
        if (duration == null) return // This shouldn't ever happen, this just keeps kotlin happy

        val tag = handler.startOrUpdateTimer(target.player, Duration.ofSeconds(duration))

        val remaining = tag.getRemainingDuration()
        MessageUtil.sendInfo(issuer, "${target.player.name} is now tagged for ${remaining.toRemainingString()}")




    }


    @Subcommand("remove|untag|unset")
    @CommandPermission("alphheim.admin")
    @CommandCompletion("@players")
    fun remove(issuer: CommandSender, target: OnlinePlayer) {
        val tag = handler.getTag(target.player)
        if (tag == null) {
            MessageUtil.sendInfo(issuer, "${target.player.name} was not tagged")
        } else {
            tag.remove()
            MessageUtil.sendInfo(issuer, "Removed tag for ${target.player.name}")
        }
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}