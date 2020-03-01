/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.combat.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.racial.RacialHandler

class PotionListener(private var plugin: ApertureCore) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    //@EventHandler
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