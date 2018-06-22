/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.BaseCommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player
import java.time.Duration
import java.time.Period
import java.util.concurrent.TimeUnit
import java.util.logging.Level

abstract class AlphheimCommand(private val plugin: AlphheimCore) : BaseCommand() {
    init {
        try {
            plugin.commandManager.registerCommand(this, true)
        } catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "Command $name failed to register!")
            ex.printStackTrace()
        }
    }


    fun checkCooldown(testUser: Player, metaKey: String): Boolean {
        val cooldown = plugin.permissionHandler.getLongMetaCached(testUser, metaKey, -1L)
        val user = plugin.userManager.getUser(testUser)
        val currentTime = System.currentTimeMillis()

        if (cooldown == -1L) {
            if (!user.hasOverrides()) {
                MessageUtil.sendError(testUser, "An internal error has occurred, Please contact electronicboy!")
                plugin.logger.log(Level.WARNING, "User ${testUser.name} attempted to use command, but could not find cooldown key! ")
                return false
            }
        } else {
            if (user.hasOverrides()) {
                return true
            }
            val cooldownTime = user.getCooldown(metaKey)
            val time = if (cooldownTime != null) {
                1000 * ((cooldownTime + 500) / 1000)
            } else {
                null
            }
            if (time == null || currentTime + 500 >= time) {
                user.setCooldown(metaKey, currentTime + TimeUnit.SECONDS.toMillis(cooldown))
            } else {
                val duration = Duration.ofMillis(time - currentTime)
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
                if (seconds != 0L || hasAppended || (!hasAppended && seconds == 0L)) {
                    if (hasAppended) sb.append(", ")
                    val secondsToDis = if (seconds == 0L) {
                        1L
                    } else seconds

                    sb.append("$secondsToDis seconds")
                }

                MessageUtil.sendInfo(testUser, sb.toString())
                return false

            }
        }
        return true
    }
}
