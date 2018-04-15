/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.BaseCommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.Level

abstract class AlphheimCommand(private val plugin: AlphheimCore, name: String) : BaseCommand(name) {
    init {
        plugin.commandManager.registerCommand(this, true)
    }


    fun checkCooldown(testUser: Player, metaKey: String): Boolean {
        val cooldown = plugin.permissionHandler.getLongMetaCached(testUser, metaKey, -1L)
        val user = plugin.userManager.getUser(testUser)

        if (cooldown == -1L) {
            if (!user.hasOverrides()) {
                MessageUtil.sendError(testUser, "An internal error has occurred, Please contact electronicboy!")
                plugin.logger.log(Level.WARNING, "User ${testUser.name} attempted to use command, but could not find cooldown key! ")
                return false
            }
        } else {
            val time = user.getCooldown(metaKey)
            println("timestamp: $time")
            if (time == null || System.currentTimeMillis() >= time) {
                user.setCooldown(metaKey, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cooldown))
            } else {
                val duration = Duration.ofMillis(time - System.currentTimeMillis())
                val seconds = (duration.toMillis() / 1000) % 60
                val minutes = duration.toMinutes() % 60
                val hours = duration.toHours() % 24

                val sb = StringBuilder("You cannot cast this spell for another ")
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

                MessageUtil.sendInfo(testUser, sb.toString())
                return false

            }
        }
        return true
    }
}
