/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.commands

import co.aikar.commands.BaseCommand
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MessageUtil
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.Level

abstract class CoreCommand(private val plugin: ApertureCore) : BaseCommand() {
    init {
        try {
            plugin.commandManager.registerCommand(this, true)
        } catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "Command $name failed to register!")
            ex.printStackTrace()
        }
    }


    // Todo: Move to dedicated manager/permission handler (maybe)
    fun checkCooldown(testUser: Player, metaKey: String, silent: Boolean = false): Boolean {
        val permissionHandler = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!
        val userManager = plugin.componentHandler.getComponent(UserManager::class.java)!!

        val cooldown = permissionHandler.getLongMetaCached(testUser, metaKey, -1L)
        val user = userManager.getUser(testUser)
        val currentTime = System.currentTimeMillis()

        if (cooldown == -1L) {
            if (!user.hasOverrides() && testUser.hasPermission("aperture.cooldowns.override.$metaKey")) {
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
                if (!silent) {
                    val sb = StringBuilder("You cannot cast this spell for another ")
                    sb.append(MessageUtil.durationToString(Duration.ofMillis(time - currentTime)))
                    MessageUtil.sendInfo(testUser, sb.toString())
                }
                return false

            }
        }
        return true
    }


    companion object {

    }
}
