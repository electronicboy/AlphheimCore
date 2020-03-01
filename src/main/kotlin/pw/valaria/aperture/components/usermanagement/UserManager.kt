/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.usermanagement

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.usermanagement.listeners.UserListener
import pw.valaria.aperture.data.AlphheimUser
import pw.valaria.aperture.utils.MySQL
import java.util.*
import java.util.concurrent.TimeUnit

class UserManager(plugin: ApertureCore) : AbstractHandler(plugin){
    private val userCache: LoadingCache<UUID, AlphheimUser>

    init {
        userCache = CacheBuilder.newBuilder().maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(object : CacheLoader<UUID, AlphheimUser>() {
                    override fun load(uuid: UUID): AlphheimUser {
                        return AlphheimUser(uuid)
                    }
                })

        MySQL.executor.execute{
            plugin.server.onlinePlayers.forEach { getUser(it) }
        }

        UserListener(this)

    }

    fun getUser(uuid: UUID): AlphheimUser {
        return userCache.get(uuid)
    }

    fun getUser(sender: Player): AlphheimUser {
        return getUser(sender.uniqueId)
    }

    fun getOnlineUsers(): List<AlphheimUser> {
        return plugin.server.onlinePlayers.map { getUser(it) }.toList()
    }


}