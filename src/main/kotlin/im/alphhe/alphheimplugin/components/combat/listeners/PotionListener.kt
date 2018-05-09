/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat.listeners

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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
    fun playerSplash(e: PotionSplashEvent) {
        // Handle reapplication of status effects

        // if there is no invis added by the pot, escape here
        val pot = e.potion.effects.find { pot -> pot.type == PotionEffectType.INVISIBILITY }
                ?: return

        e.affectedEntities.stream().filter { entity -> entity is Player }.forEach { entity ->
            object : BukkitRunnable() {
                override fun run() {
                    val p = entity as Player
                    plugin.racialHandler.applyEffects(p)
                }
            }.runTask(plugin)

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


        object : BukkitRunnable() {
            override fun run() {
                plugin.racialHandler.applyEffects(e.player)
            }
        }.runTask(plugin)

        val potionMeta = e.item.itemMeta as PotionMeta

        if (potionMeta.hasCustomEffect(PotionEffectType.INVISIBILITY)) {
            potionMeta.removeCustomEffect(PotionEffectType.INVISIBILITY)
            e.item.itemMeta = potionMeta

        }


    }

}