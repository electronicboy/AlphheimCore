/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests.storage

import co.aikar.taskchain.TaskChain
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chests.inventory.PlayerChest
import pw.valaria.aperture.utils.MySQL
import java.util.*
import java.util.concurrent.CompletableFuture

class MysqlChestStorage(val plugin: ApertureCore) : IChestStorage {


    override fun getChestsForUser(uuid: UUID): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()

        getChain(uuid).asyncFirst {
            val list = ArrayList<String>()
            MySQL.getConnection().use { conn ->

                conn.prepareStatement("SELECT CHEST_NAME FROM player_chests INNER JOIN player_data pd on player_chests.PLAYER_ID = pd.PLAYER_ID WHERE pd.PLAYER_UUID = ? ")
                        .use { stmt ->
                            stmt.setString(0, uuid.toString())
                            stmt.executeQuery().use { rs ->

                                while (rs.next()) {
                                    val chestName = rs.getString("CHEST_NAME")
                                    list.add(chestName)
                                }



                            }
                        }
            }

            future.complete(list)
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