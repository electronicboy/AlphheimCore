/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial

import com.palmergames.bukkit.towny.Towny
import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class RacialHandler(private val plugin: AlphheimCore) {
    private val enchants: Map<Player, Map<PotionEffectType, Int>> = HashMap()
    private val towny: Towny = plugin.server.pluginManager.getPlugin("Towny") as Towny


}