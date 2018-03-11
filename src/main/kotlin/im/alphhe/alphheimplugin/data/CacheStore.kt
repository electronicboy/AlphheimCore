/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package im.alphhe.alphheimplugin.data

import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shane on 11/23/16.
 * Created for Valaria
 */
class CacheStore {
    private val uuidPlayerNameMap = ConcurrentHashMap<UUID, String>()
    private val playerUUIDMap = ConcurrentHashMap<String, UUID>()

    fun addPlayer(player: Player) {
        val name = player.name.toLowerCase()
        val uuid = player.uniqueId

        uuidPlayerNameMap.put(uuid, name)
        playerUUIDMap.put(name, uuid)

    }

    fun addEntry(uuid: UUID, name: String) {
        uuidPlayerNameMap.put(uuid, name)
        playerUUIDMap.put(name, uuid)

    }


    fun getName(uuid: UUID): String? {
        return uuidPlayerNameMap[uuid]
    }

    fun getUniqueId(name: String): UUID? {
        for ((key, value) in uuidPlayerNameMap) {
            if (value.equals(name, ignoreCase = true))
                return key
        }

        return playerUUIDMap[name.toLowerCase()]


    }


}
