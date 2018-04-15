/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class CommandFix(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "fix") {

    @Subcommand("hand")
    @CommandAlias("fixhand")
    @CommandPermission("alphheim.fix")
    fun onHand(sender: Player) {
        val item = sender.itemInHand
        if (item == null || item.type.isBlock || item.durability == 0.toShort() || item.type.maxDurability < 1) {
            MessageUtil.sendError(sender, "You cannot repair this item!")
            return
        }

        val cooldown = plugin.permissionHandler.getLongMetaCached(sender, "fixCooldown", -1L)
        val user = plugin.userManager.getUser(sender)

        if (cooldown == -1L) {
            if (!user.hasOverrides()) {
                MessageUtil.sendError(sender, "An internal error has occurred, Please contact electronicboy!")
                plugin.logger.log(Level.WARNING, "User ${sender.name} attempted to use command, but could not find cooldown key! ")
                return
            }
        } else {
            val time = user.getCooldown("fixCooldown")
            println("timestamp: $time")
            if (time == null || System.currentTimeMillis() >= time) {
                user.setCooldown("fixCooldown", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cooldown))
            } else {
                val duration = Duration.ofMillis(time - System.currentTimeMillis())
                val seconds = (duration.toMillis() / 1000) % 60
                val minutes = duration.toMinutes() % 60
                val hours = duration.toHours() % 24

                val sb = StringBuilder("You cannot use this command for another ")
                var hasAppended = false
                if (hours != 0L) {
                    sb.append("$hours hour")
                    hasAppended = true
                }
                if (minutes != 0L || hasAppended) {
                    if (hasAppended) sb.append(", ")
                    sb.append("$minutes minutes")
                    hasAppended = true
                }
                if (seconds != 0L || hasAppended) {
                    if (hasAppended) sb.append(", ")
                    sb.append("$seconds seconds")
                }

                MessageUtil.sendInfo(sender, sb.toString())
                return

            }
        }

        item.durability = 0
        sender.itemInHand = item // This item is already there, but this solves issues with updates


    }


    @CommandAlias("fixall")
    @CommandPermission("alphheim.fixall")
    @Subcommand("all")
    fun onAll(sender: Player) {
        // This command currently isn't given out to the public, so...
        sender.inventory.contents.forEach {
            if (it != null && !it.type.isBlock && it.durability != 0.toShort() && it.type.maxDurability == 0.toShort()) {
                it.durability = 0
            }
        }

        sender.inventory.armorContents.forEach {
            if (it != null && !it.type.isBlock && it.durability != 0.toShort() && it.type.maxDurability == 0.toShort()) {
                it.durability = 0
            }
        }

        MessageUtil.sendInfo(sender, "Items repaired!")

    }


}
