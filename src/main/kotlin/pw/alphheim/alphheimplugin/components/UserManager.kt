/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.components

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import pw.alphheim.alphheimplugin.data.AlphheimUser
import pw.alphheim.alphheimplugin.utils.MySQL
import java.util.*
import java.util.concurrent.TimeUnit

class UserManager {
    private val userCache: LoadingCache<UUID, AlphheimUser>

    init {
        userCache = CacheBuilder.newBuilder().
                maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(object : CacheLoader<UUID, AlphheimUser>() {
                    override fun load(uuid: UUID): AlphheimUser {

                        return AlphheimUser(uuid)

                    }

                })
    }


}