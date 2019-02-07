/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.AlphheimCommand
import org.bukkit.entity.Player

@CommandAlias("craft")
class CommandCraft(private val plugin: ApertureCore) : AlphheimCommand(plugin) {

    @Default
    @CommandPermission("alphheim.craft")
    fun craft(sender: Player) {
        sender.openWorkbench(null, true)
    }

}