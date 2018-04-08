/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial

import com.palmergames.bukkit.towny.Towny
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.racial.handler.IRacialProcessor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import javax.print.attribute.IntegerSyntax

val HALF_INT = Int.MAX_VALUE / 2
class RacialHandler(private val plugin: AlphheimCore) {
    private val enchants: Map<Player, Map<PotionEffectType, Int>> = HashMap()
    private val getters = LinkedList<IRacialProcessor>()
    private val towny: Towny = plugin.server.pluginManager.getPlugin("Towny") as Towny

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
                if (enchant.duration < HALF_INT) { // use half int for safety
                    if (!applyMap.contains(enchant.type)) {
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

                    if (potionEffect != null && potionEffect.amplifier != entry.value) {
                        player.addPotionEffect(PotionEffect(entry.key, Int.MAX_VALUE, entry.value, true, false), true)
                    }
                }


            }




        }


    }


}