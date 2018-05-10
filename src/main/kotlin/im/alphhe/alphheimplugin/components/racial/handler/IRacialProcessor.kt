/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial.handler

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

interface IRacialProcessor {

    fun getEnchants(player: Player): Map<PotionEffectType, Int>

}