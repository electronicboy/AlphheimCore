/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.spawn.command

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.spawn.SpawnHandler

@CommandAlias("spawn")
class CommandSpawn(private val spawnHandler: SpawnHandler, private val plugin: ApertureCore) : CoreCommand(plugin) {

    @Default
    @CommandPermission("alphheim.spawn")
    fun spawn(self: Player) {
        spawnHandler.goSpawn(self, self)
    }


    //@Subcommand("other")
    @CommandAlias("spawn")
    @CommandPermission("alphheim.spawn.other")
    @CommandCompletion("@players")
    fun spawn(self: CommandSender, target: OnlinePlayer?) {
        when {
            target != null -> spawnHandler.goSpawn(target.player, self)
            self is Player -> spawnHandler.goSpawn(self, self)
            else -> println("fell through...")
        }
    }


    /*
    @Subcommand("book")
    @CommandPermission("group.developer")
    fun getBook(sender: Player) {
        sender.inventory.addItem(spawnHandler.getBook())
    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
     */
}