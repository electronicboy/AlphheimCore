/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial.handler

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class RacialEffectsProvider(private val plugin: AlphheimCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val race = plugin.permissionHandler.getMeta(player, "race", "unknown");

        return when (race) {
            "elf" -> mutableMapOf(PotionEffectType.SPEED to 0)
            "dwarf" -> mutableMapOf(PotionEffectType.FAST_DIGGING to 0)
            "human" -> mutableMapOf(PotionEffectType.WATER_BREATHING to 0)
            else -> mutableMapOf()
        }


    }


}
