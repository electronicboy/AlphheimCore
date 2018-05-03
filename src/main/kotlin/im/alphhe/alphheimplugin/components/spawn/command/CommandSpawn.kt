/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.spawn.command

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.contexts.OnlinePlayer
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class CommandSpawn(private val spawnHandler: SpawnHandler, private val plugin: AlphheimCore) : AlphheimCommand(plugin, "spawn") {

    @Default
    @CommandPermission("alphheim.spawn")
    fun spawn(self: Player) {
        spawnHandler.goSpawn(self, self)
    }


    //@Subcommand("other")
    @CommandAlias("spawnother")
    @CommandPermission("alphheim.spawn.other")
    @CommandCompletion("@players")
    fun spawn(self: CommandSender, target: OnlinePlayer?) {
        if (target != null) {
            spawnHandler.goSpawn(target.player, self)
        } else if (self is Player){
            spawnHandler.goSpawn(self, self)
        } else {
            println("fell through...")
        }
    }


}