/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.racial.handler

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.permissions.PermissionHandler
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class RacialEffectsProvider(private val plugin: ApertureCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val race = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getMeta(player, "race", "unknown")

        return when (race) {
            "elf" -> mutableMapOf(PotionEffectType.SPEED to 0)
            "dwarf" -> mutableMapOf(PotionEffectType.FAST_DIGGING to 0)
            "human" -> mutableMapOf(PotionEffectType.INCREASE_DAMAGE to 0)
            else -> mutableMapOf()
        }


    }


}
