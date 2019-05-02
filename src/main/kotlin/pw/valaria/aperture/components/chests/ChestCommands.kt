/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import net.kyori.text.TextComponent
import net.kyori.text.format.TextColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.MySQL

class ChestCommands(val plugin: ApertureCore, val chestHandler: ChestHandler) : CoreCommand(plugin) {


    @CommandAlias("chests|listchests")
    fun listChests(sender: Player) {
        val chestsForUser = chestHandler.chestStorage.getChestsForUser(sender.uniqueId);

        chestsForUser.whenCompleteAsync { chests, exception ->
            if (exception != null) {
                MessageUtil.sendError(sender, "An error occured while attempting to access your chest information, please contact staff for support")
                plugin.logger.severe("An error occured while attempting to read chest information for ${sender.uniqueId}")
                throw exception
            }


            if (chests.isEmpty()) {
                MessageUtil.sendInfo(sender, "You have no chests!")
            } else {
                MessageUtil.sendInfo(sender, "chests:")

                for (chest in chests) {
                            val text = TextComponent.builder()
                            .content("- ")
                            .color(TextColor.RED)
                            .append(TextComponent.of(chest, TextColor.GOLD))
                            .build()
                    MessageUtil.sendInfo(sender, text)
                }

            }

        }
    }

    @CommandAlias("chests|listchests")
    @CommandPermission("alphheim.admin")
    fun listChests(sender: Player, player: OfflinePlayer) {
        listChests(sender)
    }
}