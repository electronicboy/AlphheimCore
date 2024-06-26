/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.spawn.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.meta.BookMeta
import pw.valaria.aperture.components.spawn.SpawnHandler

class SpawnListener(private val spawnHandler: SpawnHandler) : Listener {


    init {
        spawnHandler.plugin.server.pluginManager.registerEvents(this, spawnHandler.plugin)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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

    @EventHandler
    fun openBookCheck(e: PlayerInteractEvent) {
        if (!e.hasItem() || e.hasBlock() && e.clickedBlock!!.type.isInteractable) return

        if (e.item!!.type != Material.WRITTEN_BOOK) return

        val bookMeta = e.item!!.itemMeta as BookMeta
        if (!bookMeta.hasTitle() || !bookMeta.title!!.contains("Welcome Guide", true)) return

        if (bookMeta.author == "Alphheim" || bookMeta.author == "Esterwilde" || bookMeta.author == "Valaria") {
            e.player.inventory.setItem(e.hand!!, spawnHandler.getBook())
        }

    }

}