/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocedits.commands

import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.mmocedits.MMOCreditsHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("mmocredits")
class CommandCredits(private val plugin: AlphheimCore, private val handler: MMOCreditsHandler) : AlphheimCommand(plugin, "credits"){

    @CommandPermission("alphheim.mod")
    fun creditCheck(sender: Player) {
        handler.getCredits(sender)
    }

    @CommandPermission("alphheim.dev")
    @CommandCompletion("@players")
    @CatchUnknown
    @Subcommand("credits|check")
    fun creditCheck(sender: Player, @Flags("defaultself") target: Player) {
        handler.getCredits(sender)

    }


    @CommandPermission("alphheim.dev")
    @CommandCompletion("@players")
    @Subcommand("give")
    fun giveCredits(sender: CommandSender, target: OfflinePlayer, amount: Int?) {
        if (amount!! <= 0) {
            MessageUtil.sendError(sender, "Specify a value greater than 0!")
        }

        handler.giveCredits(target, amount)

    }


}
