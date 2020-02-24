/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.racial

import com.palmergames.bukkit.towny.Towny
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.racial.handler.MinerSkillProvider
import pw.valaria.aperture.components.racial.handler.IRacialProcessor
import pw.valaria.aperture.components.racial.listeners.RacialPlayerListener
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

const val HALF_INT = Int.MAX_VALUE / 2

class RacialHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    //private val enchants: Map<Player, Map<PotionEffectType, Int>> = HashMap()
    private val getters = LinkedList<IRacialProcessor>()
    private val towny: Towny = plugin.server.pluginManager.getPlugin("Towny") as Towny

    init {
        addHandler(MinerSkillProvider(plugin))
        //addHandler(RacialEffectsProvider(plugin))
        RacialPlayerListener(this)

        if (plugin.server.onlinePlayers.isNotEmpty()) {
            object : BukkitRunnable() {
                override fun run() {
                    for (p in plugin.server.onlinePlayers) {
                        applyEffects(p)
                    }
                }

            }.runTask(plugin)
        }
    }


    fun addHandler(handler: IRacialProcessor) {
        getters.add(handler)
    }

    fun removeHandler(handler: IRacialProcessor): Boolean {
        return getters.remove(handler)
    }

    fun applyEffects(player: Player) {
        val applyMap = HashMap<PotionEffectType, Int>()

        for (getter in getters) {
            val enchants1 = getter.getEnchants(player)

            // generate mappings
            for (entry in enchants1) {
                val currentEntry = applyMap[entry.key]
                if (currentEntry == null || currentEntry <= entry.value) {
                    applyMap[entry.key] = entry.value
                }
            }
            // remove effects players no longer need
            for (enchant in player.activePotionEffects) {
                if (enchant.duration > HALF_INT) { // use half int for safety
                    if (enchant.type != PotionEffectType.INVISIBILITY && !applyMap.contains(enchant.type)) {
                        player.removePotionEffect(enchant.type)
                    }
                }
            }

            // apply effects that they don't have
            val activePotionEffects = player.activePotionEffects
            val activeEffects = activePotionEffects.map { it.type }.toHashSet()
            for (entry in applyMap) {
                if (!activeEffects.contains(entry.key)) {
                    player.addPotionEffect(PotionEffect(entry.key, Int.MAX_VALUE, entry.value, true, false))
                } else {
                    var potionEffect: PotionEffect? = null

                    for (potEffect in activePotionEffects) {
                        if (potEffect.type == entry.key)
                            potionEffect = potEffect
                        break
                    }

                    if (potionEffect != null && potionEffect.amplifier <= entry.value) {
                        player.addPotionEffect(PotionEffect(entry.key, Int.MAX_VALUE, entry.value, true, false), true)
                    }
                }


            }


        }


    }


}