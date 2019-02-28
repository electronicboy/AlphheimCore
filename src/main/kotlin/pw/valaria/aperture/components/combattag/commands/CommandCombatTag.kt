/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.combattag.CombatTagHandler
import pw.valaria.aperture.toRemainingString
import pw.valaria.aperture.utils.MessageUtil


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
    fun check(issuer: CommandSender, target: OnlinePlayer) {


        val tag = handler.getTag(target.player.uniqueId)

        if (tag == null) {
            MessageUtil.sendInfo(issuer, "You are not tagged!")
        } else {
            val remaining = tag.getRemainingDuration()
            MessageUtil.sendInfo(issuer, "You are currently tagged, ${remaining.toRemainingString()} remaining!")
        }
    }


}