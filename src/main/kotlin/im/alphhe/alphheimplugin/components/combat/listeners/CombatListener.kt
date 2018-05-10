/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
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

class CombatListener(private val plugin: AlphheimCore) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun gapple(e: PlayerItemConsumeEvent) {
        if (e.item.type == Material.GOLDEN_APPLE && e.item.durability == 1.toShort()) {
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
                Bukkit.getScheduler().runTask(plugin, { e.player.updateInventory() })
            }
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
