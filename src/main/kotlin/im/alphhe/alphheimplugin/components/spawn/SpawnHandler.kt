/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.spawn

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.spawn.command.CommandSpawn
import im.alphhe.alphheimplugin.components.spawn.listeners.SpawnListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.TeleportUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class SpawnHandler(val plugin: AlphheimCore) {


    private val worldSpawn = Location(Bukkit.getWorlds()[0], 850.0, 37.0, -1696.0, 180f, 0f)
    private val selectSpawn = Location(Bukkit.getWorlds()[0], 723.0, 6.0, -1692.0, 0f, 0f)

    init {
        CommandSpawn(this, plugin)
        SpawnListener(this)

    }



    fun goSpawn(teleportee: Player, teleporter: CommandSender?, silent: Boolean = false) {
        if (teleportee == teleporter) {
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, 10, plugin).process()

        } else if (teleporter == null || teleporter is ConsoleCommandSender || silent){
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, -1, plugin).process()
        } else if (teleporter is Player ) {
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, -1, plugin).process()
            MessageUtil.sendInfo(teleportee, "You have been teleported to spawn by ${teleporter.name}")
            MessageUtil.sendInfo(teleporter, "Teleported ${teleportee.player.name} to spawn!")
        }


    }


    internal fun resolveSpawn(player: Player) : Location {
        return if (player.hasPermission("alphheim.raceselected"))
            worldSpawn
        else
            selectSpawn
    }

}