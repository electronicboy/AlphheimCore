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
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType
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


    @EventHandler
    fun playerSplash(e: PotionSplashEvent) {
        // Handle reapplication of status effects

        // if there is no invis added by the pot, escape here
        val pot = e.potion.effects.find { pot -> pot.type == PotionEffectType.INVISIBILITY }
                ?: return

        e.affectedEntities.stream().filter { entity -> entity is Player }.forEach { entity ->


            // Because apparently the client doesn't like it when you go from infinite -> finite
            e.potion.effects.forEach { eff ->
                for (activeEff in entity.activePotionEffects) {
                    if (activeEff.type == eff.type) {
                        if (activeEff.amplifier < eff.amplifier) {
                            entity.removePotionEffect(eff.type)
                        }
                    }
                }

            }

            val racialHandler = plugin.componentHandler.getComponent(RacialHandler::class.java)
            if (racialHandler != null ) {
                object : BukkitRunnable() {
                    override fun run() {
                        racialHandler.applyEffects(entity as Player)
                    }
                }.runTask(plugin)
            }

        }

        // Deal with Invis on non player entities
        e.potion.effects.forEach { potionEffect ->
            if (potionEffect.type == PotionEffectType.INVISIBILITY) {
                object : BukkitRunnable() {
                    override fun run() {
                        for (entity in e.affectedEntities) {
                            if (entity !is Player && entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                entity.removePotionEffect(PotionEffectType.INVISIBILITY)
                            }
                        }
                    }
                }.runTask(plugin)
            }
        }

    }

    @EventHandler
    fun potionConsume(e: PlayerItemConsumeEvent) {
        if (e.item.type != Material.POTION) return

        if ((e.item.itemMeta as PotionMeta).hasCustomEffect(PotionEffectType.INVISIBILITY)) return

        // Because apparently the client doesn't like it when you go from infinite -> finite
        (e.item.itemMeta as PotionMeta).customEffects.forEach { eff ->
            for (activeEff in e.player.activePotionEffects) {
                if (activeEff.type == eff.type) {
                    if (activeEff.amplifier < eff.amplifier) {
                        e.player.removePotionEffect(eff.type)
                    }
                }
            }

        }


        val racialHandler = plugin.componentHandler.getComponent(RacialHandler::class.java)
        if (racialHandler != null ) {
            object : BukkitRunnable() {
                override fun run() {
                    racialHandler.applyEffects(e.player)
                }
            }.runTask(plugin)
        }

        val potionMeta = e.item.itemMeta as PotionMeta

        if (potionMeta.hasCustomEffect(PotionEffectType.INVISIBILITY)) {
            potionMeta.removeCustomEffect(PotionEffectType.INVISIBILITY)
            e.item.itemMeta = potionMeta

        }


    }

}