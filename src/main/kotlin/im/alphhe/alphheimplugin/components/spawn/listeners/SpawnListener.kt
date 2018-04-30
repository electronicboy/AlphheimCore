/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.spawn.listeners

import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class SpawnListener(private val spawnHandler: SpawnHandler) : Listener {


    init {
        spawnHandler.plugin.server.pluginManager.registerEvents(this, spawnHandler.plugin)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!e.player.hasPlayedBefore()) {
            spawnHandler.goSpawn(e.player, null, true)
            e.player.inventory.addItem(spawnHandler.getBook())
        }


    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        e.respawnLocation = spawnHandler.resolveSpawn(e.player)
    }

}