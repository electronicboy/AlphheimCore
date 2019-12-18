/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.donor.DonorManager
import pw.valaria.aperture.components.donor.data.DonationInfoTagType
import pw.valaria.aperture.components.donor.handlers.IDonorHandler
import pw.valaria.aperture.utils.MessageUtil
import java.text.SimpleDateFormat
import java.util.*

@CommandAlias("donate")
class CommandDonor(plugin: ApertureCore, private val manager: DonorManager) : CoreCommand(plugin) {

    @CommandPermission("group.admin")
    @Subcommand("give")
    @CommandCompletion("@players @donorhandler @nothing")
    fun give(sender: CommandSender, target: OnlinePlayer, perk: IDonorHandler, @Optional args: String?) {

        val processedArgs = if (args == null) {
            mutableMapOf()
        } else {
            val arguments = mutableMapOf<String, String>()
            for (splitArgs in args.split(",")) {
                val splitedArgs = splitArgs.split("=")
                if (splitedArgs.size == 2) {
                    arguments[splitedArgs[0]] = splitedArgs[1]
                }
            }

            arguments
        }

        if (processedArgs["transaction"] == null) {
            processedArgs["transaction"] = "Manual[${sender.name}]"
        }

        perk.handle(target.player, processedArgs)
        MessageUtil.sendInfo(sender, "Giving ${perk.name} to ${target.player.name}")

    }


    @CommandPermission("group.admin")
    @Subcommand("check")
    fun check(player: Player) {
        val bukkitStack = player.inventory.itemInMainHand
        if (bukkitStack.type == Material.AIR) {
            MessageUtil.sendError(player, "You cannot check air!")
            return
        }

        val itemMeta = bukkitStack.itemMeta
        if (itemMeta == null) {
            MessageUtil.sendError(player, "Unsupported item!")
            return
        }

        val donatorInfo = itemMeta.persistentDataContainer.get(DonationInfoTagType.key, DonationInfoTagType())

        if (donatorInfo != null) {

            MessageUtil.sendInfo(player, "Transaction ID: + " + donatorInfo.transaction)

            try {
                val timestamp = donatorInfo.timestamp
                val date = Date(timestamp)
                MessageUtil.sendInfo(player, "purchased: ${SimpleDateFormat().format(date)}")
            } catch (ex: Exception) {
                MessageUtil.sendError(player, "Error occurred while fetching time")
            }

            try {
                val offlinePlayer = Bukkit.getOfflinePlayer(donatorInfo.purchaser)
                MessageUtil.sendInfo(player, "Purchased by: ${offlinePlayer.name}")
            } catch (ex: Exception) {
                MessageUtil.sendError(player, "Error occurred while fetching purchaser")
            }


        } else {
            MessageUtil.sendInfo(player, "This item does not have any donor information!")
            return
        }





    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }

}
