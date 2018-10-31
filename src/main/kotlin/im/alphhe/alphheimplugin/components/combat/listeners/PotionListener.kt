/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.racial.RacialHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.LingeringPotionSplashEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable

class PotionListener(private var plugin: AlphheimCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onExpire(e: EntityPotionEffectEvent) {
        if (e.entity !is Player) return
        val racialHandler = plugin.componentHandler.getComponent(RacialHandler::class.java) ?: return
        object : BukkitRunnable() {
            override fun run() {
                racialHandler.applyEffects(e.entity as Player)
            }
        }.runTask(plugin)
    }

    @EventHandler(ignoreCancelled = true)
    fun potionEffectApply(e: EntityPotionEffectEvent) {
        if (e.entity !is Player || e.cause == EntityPotionEffectEvent.Cause.PLUGIN) return
        if (e.action == EntityPotionEffectEvent.Action.ADDED) {
            val type = e.newEffect?.type
            // check type and that this isn't being added by a plugin
            if (type == PotionEffectType.INVISIBILITY) {
                e.isCancelled = true
            }
        } else if (e.action == EntityPotionEffectEvent.Action.REMOVED) {
            plugin.componentHandler.getComponent(RacialHandler::class.java)?.applyEffects(e.entity as Player)
        }

    }


}