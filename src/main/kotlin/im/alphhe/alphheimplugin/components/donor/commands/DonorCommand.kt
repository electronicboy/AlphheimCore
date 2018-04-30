/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.commands

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.donor.DonorManager
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.util.*

class DonorCommand(plugin: AlphheimCore, private val manager: DonorManager) : AlphheimCommand(plugin, "donate") {

    @CommandPermission("alphheim.admin")
    @Subcommand("give")
    fun give(sender: CommandSender, target: OnlinePlayer, @Single perk: String, @Optional args: String?) {
        val handler = manager.getHandler(perk)
        if (handler != null) {

            val processedArgs = if (args == null) {
                mutableMapOf<String, String>()
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



            handler.handle(target.player, processedArgs)
            MessageUtil.sendInfo(sender, "Giving $perk to ${target.player.name}")
        } else {
            MessageUtil.sendError(sender, "$perk not found in handler!")
        }

    }


    @CommandPermission("alphheim.admin")
    @Subcommand("check")
    fun check(player: Player) {
        val bukkitStack = player.itemInHand
        if (bukkitStack == null) {
            MessageUtil.sendError(player, "You cannot check air!")
        }

        val vanillaStack = CraftItemStack.asNMSCopy(bukkitStack)

        val tag = vanillaStack.tag
        if (tag == null) {
            MessageUtil.sendError(player, "This is not a tagged stack!")
            return
        }

        MessageUtil.sendInfo(player, "Fetching information....")

        if (tag.hasKey("transaction")) {
            MessageUtil.sendInfo(player, "Transaction ID: ${tag.getString("transaction")}")
        }

        if (tag.hasKey("timestamp")) {
            try {
                val timestamp = tag.getLong("timestamp")
                val date = Date(timestamp)
                MessageUtil.sendInfo(player, "purchased: ${SimpleDateFormat().format(date)}")
            } catch (ex: Exception) {
                MessageUtil.sendError(player, "Error occured while fetching time")
            }
        }

        if (tag.hasKey("purchaser")) {
            try {
                val stringUUID = tag.getString("purchaser")
                val offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(stringUUID))
                MessageUtil.sendInfo(player, "Purchased by: ${offlinePlayer.name}")
            } catch (ex: Exception) {
                MessageUtil.sendError(player, "Error occured while fetching purchaser")
            }

        }
        tag.setString("purchaser", player.uniqueId.toString())
    }


}
