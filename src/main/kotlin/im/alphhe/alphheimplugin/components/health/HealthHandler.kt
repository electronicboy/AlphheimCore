/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.health

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import net.md_5.bungee.api.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class HealthHandler(plugin: EladriaCore) : AbstractHandler(plugin) {

    fun updateHealth(player: Player) {
        var health = workHealth(player)

        if (health == -1.0) {
            plugin.logger.info(ChatColor.DARK_RED.toString() + "Health not registered under user: " + player.name)
            health = 20.0
        }

        applyHealth(player, health)
    }


    private fun applyHealth(player: Player, health: Double) {
        val healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (healthAttribute.baseValue != health) {
            healthAttribute.baseValue = health
        }

    }

    private fun workHealth(player: Player): Double {
        val user = this.plugin.luckPermsApi.userManager.getUser(player.uniqueId) ?: plugin.luckPermsApi.userManager.loadUser(player.uniqueId).join()
        val contextsForPlayer = plugin.luckPermsApi.getContextsForPlayer(player)

        val health = user.cachedData.getMetaData(contextsForPlayer).meta.getOrDefault("health", "20")
        return health.toDouble()
    }

}