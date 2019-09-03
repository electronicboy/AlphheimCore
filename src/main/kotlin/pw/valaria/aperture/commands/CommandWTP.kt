/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.utils.MessageUtil

class CommandWTP(plugin: ApertureCore) : CoreCommand(plugin) {


    @CommandAlias("wtp")
    @CommandPermission("aperture.wtp")
    @CommandCompletion("%worlds")
    fun wtp(sender: Player, target: String) {
        val world = Bukkit.getWorld(target)
        if (world == null) {
            MessageUtil.sendError(sender, "Could not find world $target")
        } else {
            val spawnLoc = world.spawnLocation
            sender.teleport(spawnLoc)
        }
    }
}