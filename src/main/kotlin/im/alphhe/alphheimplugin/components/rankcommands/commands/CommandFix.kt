/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta

@CommandAlias("fix|repair")
class CommandFix(private val plugin: AlphheimCore) : AlphheimCommand(plugin) {

    @Subcommand("hand")
    @CommandAlias("fixhand")
    @CommandPermission("alphheim.fix")
    fun onHand(sender: Player) {
        val item = sender.inventory.itemInMainHand
        if (item == null || item !is Damageable) {
            MessageUtil.sendError(sender, "You cannot repair this item!")
            return
        }

        if (checkCooldown(sender, "fixCooldown")) {
            val damageableMeta = item.itemMeta as Damageable

            if (damageableMeta.hasDamage()) {
                damageableMeta.damage = 0
                // This is stupid.
                if (damageableMeta is ItemMeta) {
                    item.itemMeta = damageableMeta
                }
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
