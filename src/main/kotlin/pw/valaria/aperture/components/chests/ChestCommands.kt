/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.utils.MessageUtil

class ChestCommands(val plugin: ApertureCore, val chestManager: ChestManager) : CoreCommand(plugin) {


    @CommandAlias("chests|listchests")
    fun listChests(sender: Player) {
        chestManager.chestStorage.getChestsForUser(sender.uniqueId).whenCompleteAsync() { chests, exception ->
            if (exception != null) {
                MessageUtil.sendError(sender, "An error occured while attempting to access your chest information, please contact staff for support")
                plugin.logger.severe("An error occured while attempting to read chest information for ${sender.uniqueId}")
                exception.printStackTrace()
                return@whenCompleteAsync
            }

            net.kyori.text.TextComponent.builder()

        }
    }

    @CommandAlias("chests|listchests")
    @CommandPermission("alphheim.admin")
    fun listChests(sender: Player, player: OfflinePlayer) {

    }
}