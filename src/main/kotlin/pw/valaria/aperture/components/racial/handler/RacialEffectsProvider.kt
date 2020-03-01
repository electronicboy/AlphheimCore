/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.racial.handler

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import pw.valaria.aperture.ApertureCore

class RacialEffectsProvider(private val plugin: ApertureCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {

        return when {
            player.hasPermission("group.elf") -> mutableMapOf(PotionEffectType.SPEED to 0)
            player.hasPermission("group.dwarf") -> mutableMapOf(PotionEffectType.FAST_DIGGING to 0)
            player.hasPermission("group.human") -> mutableMapOf(PotionEffectType.INCREASE_DAMAGE to 0)
            else -> mutableMapOf()
        }


    }


}
