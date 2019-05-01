/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests.storage

import org.bukkit.inventory.Inventory
import pw.valaria.aperture.components.chests.inventory.PlayerChest
import java.util.*
import java.util.concurrent.CompletableFuture

interface IChestStorage {


    fun getChestsForUser(uuid: UUID): CompletableFuture<List<String>>


    fun getChest(uuid: UUID, id: String?): CompletableFuture<PlayerChest>

    fun saveChest(chest: PlayerChest): CompletableFuture<Boolean>
}