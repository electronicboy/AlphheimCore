/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.health

import im.alphhe.alphheimplugin.AlphheimCore
import me.lucko.luckperms.api.Contexts
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.util.*

class HealthHandler(private var plugin: AlphheimCore) {

    private val playerHealth: MutableMap<UUID, Double>

    init {
        playerHealth = HashMap()
    }

    fun updateHealth(player: Player) {
        val pUUID = player.uniqueId
        val health = workHealth(player)

        if (health == -1.0) {
            plugin.logger.info(ChatColor.DARK_RED.toString() + "Health not registered under user: " + player.name)
            playerHealth[pUUID] = 10.0
        } else {
            playerHealth[pUUID] = health
        }

        applyHealth(player)
    }


    private fun applyHealth(player: Player) {
        val health = playerHealth[player.uniqueId] ?: return

        if (player.maxHealth != health) {
            player.maxHealth = health
        }

    }

    private fun workHealth(player: Player): Double {
        val user = this.plugin.luckPermsApi.userManager.getUser(player.uniqueId) ?: return -1.0
        val health = user.cachedData.getMetaData(Contexts.global()).meta.getOrDefault("health", "20")
        return health.toDouble()
    }

}