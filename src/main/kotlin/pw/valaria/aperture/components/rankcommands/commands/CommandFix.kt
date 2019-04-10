/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.rankcommands.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.utils.MessageUtil

@CommandAlias("fix|repair")
class CommandFix(private val plugin: ApertureCore) : CoreCommand(plugin) {

    @Subcommand("hand")
    @CommandAlias("fixhand")
    @CommandPermission("alphheim.fix")
    fun onHand(sender: Player) {
        val item = sender.inventory.itemInMainHand
        val damageableMeta = item.itemMeta as Damageable

        if (!damageableMeta.hasDamage()) {
            MessageUtil.sendError(sender, "You cannot repair this item!")
            return
        }


        if (checkCooldown(sender, "fixCooldown")) {
            damageableMeta.damage = 0
            // This is stupid.
            if (damageableMeta is ItemMeta) {
                item.itemMeta = damageableMeta
            }

        }

    }


    @CommandAlias("fixall")
    @CommandPermission("alphheim.fixall")
    @Subcommand("all")
    fun onAll(sender: Player) {
        if (!checkCooldown(sender, "fixCooldown")) return

        // This command currently isn't given out to the public, so...
        sender.inventory.contents.forEach { item ->
            val damagableMeta = item.itemMeta as? Damageable

            if (damagableMeta != null) {
                if (damagableMeta.hasDamage()) {
                    damagableMeta.damage = 0
                    if (damagableMeta is ItemMeta) {
                        item.itemMeta = damagableMeta
                    }
                }
            }
        }

        sender.updateInventory()

        MessageUtil.sendInfo(sender, "Items repaired!")

    }

    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }

}
