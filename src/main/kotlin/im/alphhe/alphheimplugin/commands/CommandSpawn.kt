/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.*
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.TeleportUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player


class CommandSpawn(val plugin: AlphheimCore) : AlphheimCommand(plugin, "spawn") {
    val spawn = Location(Bukkit.getWorlds()[0], 850.0, 37.0, -1696.0, 180f, 0f)

    @Default
    @CommandPermission("alphheim.spawn")
    fun spawn(self: Player) {
        TeleportUtil(self, spawn, 10, plugin).process()
    }


    //@Subcommand("other")
    @CommandAlias("spawnother")
    @CommandPermission("alphheim.spawn.other")
    @CommandCompletion("@players")
    fun spawn(self: Player, target: OnlinePlayer?) {

        if (target != null) {
            if (self.hasPermission("alphheim.spawn.other")) {
                target.player.teleport(spawn)
                MessageUtil.sendInfo(target.player, "You have been teleported to spawn by ${self.name}")
                MessageUtil.sendInfo(self, "Teleported ${target.player.name} to spawn!")
            } else {
                MessageUtil.sendError(self, "You do not have perms to teleport another player!")
            }
        } else {
            if (self.hasPermission("alphheim.spawn")) {
                TeleportUtil(self, spawn, 10, plugin).process()
            } else {
                MessageUtil.sendError(self, "You do not have permission to use this command!")
            }
        }

    }


}