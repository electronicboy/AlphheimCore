/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.utils

import pw.valaria.aperture.ApertureCore
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TeleportUtil(var player: Player, private val target: Location, timer: Int, private val plugin: ApertureCore) : BukkitRunnable() {
    var countdown = timer
    var loc: Location = player.location.clone()

    override fun run() {
        if (countdown > 0) {
            countdown--

            if (loc.distance(player.location) > 1) {
                cancel()
                MessageUtil.sendError(player, "Teleportation cancelled!")
            }
        } else {
            cancel()
            player.teleport(target)
        }

    }

    fun process() {
        if (countdown <= 0 || player.hasPermission("alphheim.teleport.bypass")) {
            player.teleport(target)
        } else {
            MessageUtil.sendInfo(player, "The spell will take $countdown seconds to cast...")
            this.runTaskTimer(plugin, 0, 20)
        }
    }


}
