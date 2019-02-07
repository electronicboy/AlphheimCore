/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.diversions.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("roulette")
class CommandRoulette(val plugin: ApertureCore) : CoreCommand(plugin) {

    private val random = Random()

    @Default
    @CommandPermission("alphheim.diversions")
    fun roulette(self: Player) {
        if (random.nextFloat() > 0.8) {
            broadcast(self.location, self.name + " pulls the trigger *BANG*")
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { self.kickPlayer("*BANG*") }, 2L)
        } else {
            broadcast(self.location, self.name + " pulls the trigger *CLICK*")
        }
    }

    private fun broadcast(loc: Location, msg: String) {
        Bukkit.getOnlinePlayers().filter { p -> loc.world == p.location.world && loc.distance(p.location) < 50 }.forEach { MessageUtil.sendInfo(it, msg) }
    }


}

