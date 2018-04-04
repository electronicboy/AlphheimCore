/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class CommandRoulette(val plugin: AlphheimCore) : AlphheimCommand(plugin, "roulette") {

    private val random = Random()

    @Default
    @CommandPermission("alphheim.diversions")
    fun roulette(self: Player) {
        val i = random.nextInt(101)

        if (i > 80) {
            broadcast(self.location, self.getName() + " pulls the trigger *BANG*")
            Bukkit.getScheduler().runTaskLater(plugin, { self.kickPlayer("*BANG*") }, 2L)
        } else {
            broadcast(self.location, self.getName() + " pulls the trigger *CLICK*")
        }
    }

    private fun broadcast(loc: Location, msg: String) {
        Bukkit.getOnlinePlayers().filter { p -> loc.world == p.location.world && loc.distance(p.location) < 50 }.forEach { MessageUtil.sendInfo(it, msg) }
    }




}

