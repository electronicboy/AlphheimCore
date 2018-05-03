/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.racial.handler

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class DwarvenRacialProvider(private var plugin: AlphheimCore) : IRacialProcessor{
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val groupsForUser = plugin.permissionHandler.getOwnGroupsForUser(player)

        val groups = groupsForUser.map { group -> group.name.toLowerCase() }


        return if (groups.contains("miner") || groups.contains("skrati") || groups.contains("corveil")) {
            mutableMapOf( PotionEffectType.FAST_DIGGING to 1)
        } else if (groups.contains("taskmaster") || groups.contains("boki") || groups.contains("tolaes")) {
            mutableMapOf( PotionEffectType.FAST_DIGGING to 2)
        } else mutableMapOf()

    }


}