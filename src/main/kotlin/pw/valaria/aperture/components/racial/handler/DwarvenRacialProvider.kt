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

class DwarvenRacialProvider(private var plugin: ApertureCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val groupsForUser = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getOwnGroupsForUser(player)

        val groups = groupsForUser.map { group -> group.name.toLowerCase() }


        return if (groups.contains("miner") || groups.contains("skrati") || groups.contains("corveil")) {
            mutableMapOf(PotionEffectType.FAST_DIGGING to 1)
        } else if (groups.contains("taskmaster") || groups.contains("boki") || groups.contains("tolaes")) {
            mutableMapOf(PotionEffectType.FAST_DIGGING to 2)
        } else mutableMapOf()

    }


}