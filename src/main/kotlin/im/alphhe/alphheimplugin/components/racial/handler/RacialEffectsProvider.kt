/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial.handler

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class RacialEffectsProvider(private val plugin: EladriaCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val race = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getMeta(player, "race", "unknown");

        return when (race) {
            "elf" -> mutableMapOf(PotionEffectType.SPEED to 0)
            "dwarf" -> mutableMapOf(PotionEffectType.FAST_DIGGING to 0)
            "human" -> mutableMapOf(PotionEffectType.INCREASE_DAMAGE to 0)
            else -> mutableMapOf()
        }


    }


}
