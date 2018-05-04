/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.collect.ImmutableCollection
import com.google.inject.Singleton
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.data.AlphheimUser
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit

@Singleton
class UserManager(private val plugin: AlphheimCore) {
    private val userCache: LoadingCache<UUID, AlphheimUser>

    init {
        userCache = CacheBuilder.newBuilder().maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(object : CacheLoader<UUID, AlphheimUser>() {
                    override fun load(uuid: UUID): AlphheimUser {
                        return AlphheimUser(uuid)
                    }
                })

        MySQL.executor.execute({
            plugin.server.onlinePlayers.forEach { getUser(it) }
        })

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