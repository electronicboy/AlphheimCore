/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocredits.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.mmocredits.MMOCreditsHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("credits|mmocredits")
class CommandCredits(private val plugin: AlphheimCore, private val handler: MMOCreditsHandler) : AlphheimCommand(plugin) {


    @CommandCompletion("@players")
    @CommandPermission("alphheim.admin")
    @Subcommand("credits|check")
    fun creditCheck(sender: CommandSender, target: OfflinePlayer) {
        MySQL.executor.execute {
            val credits = handler.getCredits(target)
            MessageUtil.sendInfo(sender, "${target.name} has $credits credits!")
        }

    }

    @Default
    @Subcommand("credits|check")
    fun creditCheck(sender: Player) {
        MySQL.executor.execute {
            val credits = handler.getCredits(sender)
            MessageUtil.sendInfo(sender, "You have $credits credits to redeem!")
        }

    }


    @CommandPermission("alphheim.admin")
    @CommandCompletion("@players")
    @Subcommand("give")
    fun giveCredits(sender: CommandSender, target: OfflinePlayer, amount: Int?) {
        if (amount!! <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
            return
        }
        MySQL.executor.execute {

            if (handler.giveCredits(target, amount)) {
                if (target.isOnline) MessageUtil.sendInfo(target.player, "You have received $amount credits!")

                MessageUtil.sendInfo(sender, "You have given ${target.name} $amount credits!")
            }
        }

    }


    @CommandPermission("alphheim.admin")
    @CommandCompletion("@players")
    @Subcommand("take")
    fun takeCredits(sender: CommandSender, target: OfflinePlayer, amount: Int?) {
        if (amount!! <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
            return
        }
        MySQL.executor.execute {

            if (handler.takeCredits(target, amount)) {
                if (target.isOnline) MessageUtil.sendInfo(target.player, "You have lost $amount credits!")

                MessageUtil.sendInfo(sender, "You have taken $amount credits from ${target.name}!")
            }
        }

    }

    @CommandCompletion("@mmoskills")
    @Subcommand("redeem")
    fun redeem(sender: Player, skill: String, amount: Int?) {
        amount!!
        if (amount <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
            return
        }

        MySQL.executor.execute {
            if (handler.redeemCredits(sender, skill, amount)) {
                MessageUtil.sendInfo(sender, "You have successfully redeemed your credits!")
            } else {
                MessageUtil.sendError(sender, "You do not have enough credits!")
            }

        }

    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }


}
