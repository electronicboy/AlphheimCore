/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.combat.listeners

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class CombatListener(private val plugin: ApertureCore) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun gapple(e: PlayerItemConsumeEvent) {
        if (e.item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            if (!checkCooldown(e.player, "gappleCooldown")) {
                e.isCancelled = true

            }


        }
    }

    @EventHandler
    fun enderpearl(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_AIR && e.item?.type == Material.ENDER_PEARL) {
            if (!checkCooldown(e.player, "enderpearlCooldown")) {
                e.isCancelled = true
                Bukkit.getScheduler().runTask(plugin,  Runnable { e.player.updateInventory() })
            }
        }


    }


    fun checkCooldown(testUser: Player, metaKey: String): Boolean {
        val cooldown = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getLongMetaCached(testUser, metaKey, -1L)
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(testUser)
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

                val sb = StringBuilder("You cannot consume this item for another ")
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
