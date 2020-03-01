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
import pw.valaria.aperture.components.permissions.PermissionHandler

class MinerSkillProvider(private var plugin: ApertureCore) : IRacialProcessor {
    override fun getEnchants(player: Player): Map<PotionEffectType, Int> {
        val groupsForUser = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getOwnGroupsForUser(player)

        val groups = groupsForUser.map { group -> group.name.toLowerCase() }


        return if (groups.contains("miner") || groups.contains("eminer") || groups.contains("dminer")) {
            mutableMapOf(PotionEffectType.FAST_DIGGING to 0)
        } else if (groups.contains("grandminer") || groups.contains("egrandminer") || groups.contains("dgrandminer")) {
            mutableMapOf(PotionEffectType.FAST_DIGGING to 1)
        } else mutableMapOf()

    }


}