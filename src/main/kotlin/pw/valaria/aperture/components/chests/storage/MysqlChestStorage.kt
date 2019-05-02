/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests.storage

import co.aikar.taskchain.TaskChain
import com.google.common.cache.CacheBuilder
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chests.inventory.PlayerChest
import pw.valaria.aperture.utils.MySQL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class MysqlChestStorage(val plugin: ApertureCore) : IChestStorage {

    val playerChestListCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).expireAfterAccess(10, TimeUnit.SECONDS).build<UUID, List<String>>()

    override fun getChestsForUser(uuid: UUID): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()

        val cacheEntry = playerChestListCache.getIfPresent(uuid)

        if (cacheEntry != null) {
            future.complete(cacheEntry)
        } else {

            getChain(uuid).asyncFirst {
                val list = ArrayList<String>()
                try {
                    MySQL.getConnection().use { conn ->

                        conn.prepareStatement("SELECT CHEST_NAME FROM player_chests INNER JOIN player_data pd on player_chests.PLAYER_ID = pd.PLAYER_ID WHERE pd.PLAYER_UUID = ? ")
                                .use { stmt ->
                                    stmt.setString(1, uuid.toString())
                                    stmt.executeQuery().use { rs ->

                                        while (rs.next()) {
                                            val chestName = rs.getString("CHEST_NAME")
                                            list.add(chestName)
                                        }


                                    }
                                }
                    }

                    playerChestListCache.put(uuid, list)
                    future.complete(list)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    future.completeExceptionally(ex)
                }


            }.execute()
        }

        return future

    }


    override fun getChest(uuid: UUID, id: String?): CompletableFuture<PlayerChest> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveChest(chest: PlayerChest): CompletableFuture<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getChain(uuid: UUID): TaskChain<Any> {
        return plugin.taskChainFactory.newSharedChain<Any>("chest-$uuid")
    }

}