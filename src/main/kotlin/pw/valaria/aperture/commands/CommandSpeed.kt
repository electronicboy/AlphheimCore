/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.utils.MessageUtil

class CommandSpeed(plugin: ApertureCore) : CoreCommand(plugin) {

    @CommandAlias("speed")
    @CommandPermission("aperture.speed")
    fun speed(sender: Player) {
        if (sender.isFlying) {
            MessageUtil.sendInfo(sender, "current fly speed is ${sender.flySpeed * 10}")
        } else {
            MessageUtil.sendInfo(sender, "current walking speed is ${sender.flySpeed * 10}")
        }
    }


    @CommandAlias("speed")
    @CommandPermission("aperture.speed")
    fun speed(sender: Player, value: Float) {
        var target: Float = value / 10;
        if (sender.isFlying) {
            sender.flySpeed = target
            MessageUtil.sendInfo(sender, "current fly speed is now ${sender.flySpeed * 10}")
        } else {
            sender.walkSpeed = target
            MessageUtil.sendInfo(sender, "current walking speed is now ${sender.walkSpeed * 10}")
        }
    }
}