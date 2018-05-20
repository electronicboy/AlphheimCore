/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocredits.commands

import co.aikar.commands.annotation.*
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.mmocredits.MMOCreditsHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("mmocredits")
class CommandCredits(private val plugin: AlphheimCore, private val handler: MMOCreditsHandler) : AlphheimCommand(plugin, "credits") {

    @CommandPermission("alphheim.dev")
    @CommandCompletion("@players")
    @Default
    @Subcommand("credits|check")
    fun creditCheck(sender: Player, @Flags("defaultself") target: Player) {
        MySQL.executor.execute({
            val credits = handler.getCredits(target.player)
            if (target.player == sender) {
                MessageUtil.sendInfo(sender, "You have $credits credits to redeem!")
            } else {
                MessageUtil.sendInfo(sender, "${target.player.name} has $credits")
            }
        })

    }


    @CommandPermission("alphheim.dev")
    @CommandCompletion("@players")
    @Subcommand("give")
    fun giveCredits(sender: CommandSender, target: OfflinePlayer, amount: Int?) {
        if (amount!! <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
        }
        MySQL.executor.execute({

            if (handler.giveCredits(target, amount)) {
                if (target.isOnline) MessageUtil.sendInfo(target.player, "You have recieved $amount credits!")

                MessageUtil.sendInfo(sender, "You have given ${target.name} $amount credits!")
            }
        })

    }


    @CommandPermission("alphheim.dev")
    @CommandCompletion("@players")
    @Subcommand("take")
    fun takeCredits(sender: CommandSender, target: OfflinePlayer, amount: Int?) {
        if (amount!! <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
        }
        MySQL.executor.execute({

            if (handler.takeCredits(target, amount)) {
                if (target.isOnline) MessageUtil.sendInfo(target.player, "You have lost $amount credits!")

                MessageUtil.sendInfo(sender, "You have taken $amount credits from ${target.name}!")
            }
        })

    }

    @CommandPermission("alphheim.dev")
    @CommandCompletion("@mmoskills")
    @Subcommand("redeem")
    fun redeem(sender: Player, skill: String, amount: Int?) {
        amount!!
        if (amount <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
        }

        MySQL.executor.execute({
            if (handler.redeemCredits(sender, skill, amount)) {
                MessageUtil.sendInfo(sender, "You have successfully redeemed your credits!")
            } else {
                MessageUtil.sendError(sender, "You do not have enough credits!")
            }

        })

    }


}
